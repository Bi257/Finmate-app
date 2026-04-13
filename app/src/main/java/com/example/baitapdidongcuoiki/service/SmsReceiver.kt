package com.example.baitapdidongcuoiki.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.example.baitapdidongcuoiki.data.local.AppDatabase
import com.example.baitapdidongcuoiki.data.local.entity.SmsMessageEntity
import com.example.baitapdidongcuoiki.data.repository.ExchangeRepository
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.usecase.UseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var useCases: UseCases

    @Inject
    lateinit var exchangeRepository: ExchangeRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        for (sms in messages) {
            val sender = sms.originatingAddress ?: "Unknown"
            val body = sms.messageBody ?: ""
            val timestamp = sms.timestampMillis ?: System.currentTimeMillis()
            val smsId = timestamp

            Log.d("SMS_RAW", "[$sender] $body")

            // Lưu tin nhắn vào database
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getInstance(context)
                    val entity = SmsMessageEntity(
                        id = smsId,
                        address = sender,
                        body = body,
                        date = timestamp
                    )
                    db.smsMessageDao().insert(entity)
                    Log.d("SMS_SAVE", "✅ Đã lưu tin nhắn: ${body.take(50)}")
                } catch (e: Exception) {
                    Log.e("SMS_SAVE_ERROR", "❌ Lỗi lưu tin nhắn: ${e.message}", e)
                }
            }

            // Xử lý tạo giao dịch nếu là tin nhắn ngân hàng
            val draft = parseBankSms(sender, body)
            if (draft != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val (tx, logLine) = draft.toTransactionWithConversion(exchangeRepository)
                        useCases.addTransactionUseCase(tx)
                        useCases.refreshTransactionsUseCase.invoke()
                        Log.d("SMS_SUCCESS", logLine)
                    } catch (e: Exception) {
                        Log.e("SMS_ERROR", "Lỗi tạo giao dịch: ${e.message}", e)
                    }
                }
            }
        }
    }


    private data class BankSmsDraft(
        val title: String,
        val amountRaw: Double,
        val isUsd: Boolean,
        val type: String,
        val noteBase: String
    ) {
        suspend fun toTransactionWithConversion(
            exchangeRepository: ExchangeRepository
        ): Pair<Transaction, String> {
            return if (isUsd) {
                val rate = exchangeRepository.getUsdToVndRate()
                val vnd = amountRaw * rate
                val note = noteBase + String.format(
                    Locale.US,
                    " | Quy đổi %.2f USD (1 USD ≈ %,.0f VND).",
                    amountRaw,
                    rate
                )
                val tx = Transaction(
                    id = null,
                    title = title,
                    amount = vnd,
                    type = type,
                    date = System.currentTimeMillis(),
                    note = note
                )
                tx to "Đã lưu (USD→VND): ${tx.title} | ${tx.amount} VND"
            } else {
                val tx = Transaction(
                    id = null,
                    title = title,
                    amount = amountRaw,
                    type = type,
                    date = System.currentTimeMillis(),
                    note = noteBase
                )
                tx to "Đã lưu (VND): ${tx.title} | ${tx.amount} VND"
            }
        }
    }

    private fun parseUsdAmount(body: String): Double? {
        val patterns = listOf(
            Regex("""([\d,]+(?:\.\d+)?)\s*(USD|usd)"""),
            Regex("""\$\s*([\d,]+(?:\.\d+)?)""")
        )
        for (re in patterns) {
            val m = re.find(body) ?: continue
            val num = m.groupValues[1].replace(",", "").trim()
            val v = num.toDoubleOrNull()
            if (v != null && v > 0) return v
        }
        return null
    }

    private fun looksLikeBankOrBalanceSms(lower: String): Boolean {
        val keys = listOf(
            "số dư", "so du", "tk", "tai khoan", "tài khoản", "chuyển khoản", "credit", "debit",
            "vnd", "vcb", "bidv", "balance", "account", "dollar", "usd", "biến động", "bien dong"
        )
        return keys.any { lower.contains(it) }
    }

    private fun parseBankSms(sender: String, body: String): BankSmsDraft? {
        val lower = body.lowercase()

        val usdAmount = parseUsdAmount(body)
        if (usdAmount != null && looksLikeBankOrBalanceSms(lower)) {
            val isIncome = lower.contains("+") ||
                    lower.contains("nhận") ||
                    lower.contains("credit") ||
                    lower.contains("tiền vào") ||
                    lower.contains("deposit") ||
                    lower.contains("hoàn tiền")
            val type = if (isIncome) "income" else "expense"
            val noteRegex = Regex("""(?:ND|Noi dung|nội dung|Content)[:\s]*(.*)""", RegexOption.IGNORE_CASE)
            val extractedContent = noteRegex.find(body)?.groupValues?.getOrNull(1)?.trim()
            val category = detectCategory(body, type)
            val account = Regex("""\b\d{3,6}\b""").find(body)?.value ?: "Bank"
            val balance = Regex("""(?:số dư|so du|balance)[:\s]*([\d.,\s]+(?:USD|usd|VND|vnd)?)""", RegexOption.IGNORE_CASE)
                .find(body)?.groupValues?.getOrNull(1)
            val title = if (!extractedContent.isNullOrBlank()) extractedContent else "$category - $sender"
            val note = buildString {
                append("Tài khoản: $account")
                balance?.let { append(" | Số dư: $it") }
                append(" | Gốc: $body")
            }
            return BankSmsDraft(title, usdAmount, isUsd = true, type, note)
        }

        val bankKeywords = listOf("số dư", "so du", "tk", "tai khoan", "chuyển khoản", "credit", "debit", "vnd", "vcb", "bidv")
        if (bankKeywords.none { lower.contains(it) }) return null

        val amount = Regex("""([+-]?\d{1,3}(?:[.,]\d{3})+)""")
            .find(body)
            ?.value
            ?.replace("+", "")
            ?.replace("-", "")
            ?.replace(",", "")
            ?.replace(".", "")
            ?.toDoubleOrNull() ?: return null

        val isIncome = lower.contains("+") || lower.contains("nhận") || lower.contains("ghi có") || lower.contains("tiền vào") || lower.contains("hoàn tiền")
        val type = if (isIncome) "income" else "expense"
        val noteRegex = Regex("""(?:ND|Noi dung|nội dung|Content)[:\s]*(.*)""", RegexOption.IGNORE_CASE)
        val extractedContent = noteRegex.find(body)?.groupValues?.getOrNull(1)?.trim()
        val category = detectCategory(body, type)
        val account = Regex("""\b\d{3,6}\b""").find(body)?.value ?: "Bank"
        val balance = Regex("""(?:số dư|so du)[:\s]*([\d.,]+)""", RegexOption.IGNORE_CASE)
            .find(body)?.groupValues?.getOrNull(1)
        val title = if (!extractedContent.isNullOrBlank()) extractedContent else "$category - $sender"
        val note = buildString {
            append("Tài khoản: $account")
            balance?.let { append(" | Số dư: $balance VND") }
            append(" | Gốc: $body")
        }
        return BankSmsDraft(title, amount, isUsd = false, type, note)
    }

    private fun detectCategory(text: String, type: String): String {
        val lower = text.lowercase()
        return when {
            lower.contains("lương") || lower.contains("salary") -> "Lương"
            lower.contains("grab") || lower.contains("be") || lower.contains("taxi") -> "Di chuyển"
            lower.contains("shopee") || lower.contains("lazada") || lower.contains("tiki") -> "Mua sắm"
            lower.contains("ăn") || lower.contains("food") || lower.contains("quán") || lower.contains("cafe") -> "Ăn uống"
            lower.contains("điện") || lower.contains("nước") || lower.contains("internet") -> "Hóa đơn"
            lower.contains("hoàn tiền") -> "Hoàn tiền"
            else -> if (type == "income") "Thu nhập khác" else "Chi tiêu khác"
        }
    }
}









































































