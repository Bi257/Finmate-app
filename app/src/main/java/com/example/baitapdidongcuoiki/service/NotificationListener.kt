package com.example.baitapdidongcuoiki.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.baitapdidongcuoiki.di.NotificationListenerEntryPoint
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationListener : NotificationListenerService() {

    private var addTransactionUseCase: AddTransactionUseCase? = null

    override fun onCreate() {
        super.onCreate()
        addTransactionUseCase = EntryPointAccessors.fromApplication(
            applicationContext,
            NotificationListenerEntryPoint::class.java
        ).addTransactionUseCase()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        val packageName = sbn.packageName ?: return
        val extras = sbn.notification.extras

        val title = extras.getString("android.title") ?: ""
        val text = extras.getString("android.text") ?: ""

        Log.d("NOTI_RAW", "App: $packageName")
        Log.d("NOTI_RAW", "Title: $title")
        Log.d("NOTI_RAW", "Text: $text")


        if (!isFinanceApp(packageName)) return

        val transaction = parseNotification(packageName, title, text)

        if (transaction != null) {

            Log.d("NOTI_PARSED", transaction.toString())

            // LƯU DB
            CoroutineScope(Dispatchers.IO).launch {
                addTransactionUseCase?.invoke(transaction)
            }
        }
    }

    //FILTER APP
    private fun isFinanceApp(pkg: String): Boolean {
        return pkg.contains("vcb") ||
                pkg.contains("mbbank") ||
                pkg.contains("momo") ||
                pkg.contains("techcombank") ||
                pkg.contains("vietcombank")
    }

    // PARSE CHÍNH
    private fun parseNotification(
        packageName: String,
        title: String,
        text: String
    ): Transaction? {

        val content = "$title $text".lowercase()

        val amount = extractAmount(content) ?: return null

        val type = detectType(content)
        val account = extractAccount(content)
        val category = detectCategory(content)

        return Transaction(
            title = "$category ($account)",
            amount = amount,
            type = type,
            date = System.currentTimeMillis()
        )
    }

    //PARSE TIỀN
    private fun extractAmount(text: String): Double? {
        val regex = Regex("""\d{1,3}(,\d{3})*""")
        return regex.find(text)
            ?.value
            ?.replace(",", "")
            ?.toDoubleOrNull()
    }

    // TYPE
    private fun detectType(text: String): String {
        return if (
            text.contains("nhận") ||
            text.contains("credit") ||
            text.contains("+") ||
            text.contains("tiền vào")
        ) "income" else "expense"
    }

    // ACCOUNT
    private fun extractAccount(text: String): String {
        return Regex("""\d{4,}""")
            .find(text)
            ?.value ?: "Ví"
    }

    // CATEGORY AUTO
    private fun detectCategory(text: String): String {
        return when {
            text.contains("lương") -> "Lương"
            text.contains("ăn") || text.contains("food") -> "Ăn uống"
            text.contains("grab") || text.contains("taxi") -> "Di chuyển"
            text.contains("shopee") || text.contains("lazada") -> "Mua sắm"
            text.contains("điện") || text.contains("nước") -> "Hóa đơn"
            text.contains("momo") -> "Ví điện tử"
            else -> "Khác"
        }
    }
}



















