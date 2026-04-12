package com.example.baitapdidongcuoiki.ui.screen.report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.baitapdidongcuoiki.export.ExcelExporter
import com.example.baitapdidongcuoiki.export.PdfExporter
import com.example.baitapdidongcuoiki.ui.DailyMarketViewModel
import com.example.baitapdidongcuoiki.ui.components.TransactionItem
import com.example.baitapdidongcuoiki.ui.screen.home.HomeViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportScreen(
    homeViewModel: HomeViewModel,
    marketViewModel: DailyMarketViewModel
) {
    val context = LocalContext.current
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val marketState by marketViewModel.ui.collectAsStateWithLifecycle()
    val transactions = homeState.transactions

    // Định nghĩa bảng màu theo yêu cầu
    val MauTrang = Color.White
    val MauTim = Color(0xFF9C27B0)
    val MauHong = Color(0xFFE91E63)
    val MauXanhNgoc = Color(0xFF00BCD4)
    val NenNhat = Color(0xFFF3E5F5) // Tím rất nhạt cho nền

    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    }
    val numberFormatter = remember {
        NumberFormat.getNumberInstance(Locale.US)
    }

    val currentTime = remember { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) }

    // Quy đổi nhanh
    var inputAmount by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("USD") }
    var expanded by remember { mutableStateOf(false) }
    val currencyOptions = listOf("USD", "EUR", "JPY", "Vàng (PAXG/lượng)")

    // Tỷ giá thật
    val usdRate = marketState.rates.find { it.currency == "USD" }?.buy ?: 0.0
    val eurRate = marketState.rates.find { it.currency == "EUR" }?.buy ?: 0.0
    val jpyRate = marketState.rates.find { it.currency == "JPY" }?.buy ?: 0.0
    val goldPrice = marketState.goldPricePerLuongVnd  // giá vàng thật từ PAXG

    val conversionResult = remember(inputAmount, selectedCurrency, usdRate, eurRate, jpyRate, goldPrice) {
        val amount = inputAmount.toDoubleOrNull() ?: 0.0
        when (selectedCurrency) {
            "USD" -> amount * usdRate
            "EUR" -> amount * eurRate
            "JPY" -> amount * jpyRate
            "Vàng (PAXG/lượng)" -> amount * goldPrice
            else -> 0.0
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NenNhat), // Đổi sang Tím nhạt
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // ========== THỊ TRƯỜNG ==========
        item {
            Column {
                Text("Thị trường", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MauTim)
                Text("Cập nhật $currentTime", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MauTrang),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Tỷ giá ngoại tệ (dữ liệu thật)
                    Text("Tỷ giá ngoại tệ (VCB)", fontWeight = FontWeight.Bold, color = MauTim)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (marketState.rates.isEmpty()) {
                        Text("Đang tải tỷ giá...", color = Color.Gray)
                    } else {
                        marketState.rates.forEach { rate ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${rate.currency} / VNĐ", modifier = Modifier.weight(1f))
                                Text("Mua: ${numberFormatter.format(rate.buy)}", modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                                Text("Bán: ${numberFormatter.format(rate.sell)}", modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                                Text(
                                    if (rate.changePercent >= 0) "+${rate.changePercent}%" else "${rate.changePercent}%",
                                    modifier = Modifier.weight(0.5f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                                    color = if (rate.changePercent >= 0) MauHong else MauTim
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Giá vàng (dữ liệu thật từ PAXG)
                    Text("Giá vàng hôm nay", fontWeight = FontWeight.Bold, color = MauTim)
                    Spacer(modifier = Modifier.height(4.dp))

                    if (goldPrice > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Vàng (PAXG/lượng)", modifier = Modifier.weight(1.5f))
                            Text("Tham khảo: ${numberFormatter.format(goldPrice)} VND", modifier = Modifier.weight(2f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                            // Không có % thay đổi, có thể bỏ qua hoặc hiển thị "—"
                            Text("—", modifier = Modifier.weight(0.5f), textAlign = androidx.compose.ui.text.style.TextAlign.End, color = Color.Gray)
                        }
                        Text(
                            text = marketState.goldPriceMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    } else {
                        Text("Đang tải giá vàng...", color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quy đổi nhanh (có ô nhập và spinner)
                    Text("Quy đổi nhanh", fontWeight = FontWeight.Bold, color = MauTim)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = inputAmount,
                            onValueChange = { inputAmount = it },
                            label = { Text("Số lượng") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            Button(
                                onClick = { expanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MauTim)
                            ) {
                                Text(selectedCurrency, color = MauTrang)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                currencyOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            selectedCurrency = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$inputAmount $selectedCurrency = ${numberFormatter.format(conversionResult)} VNĐ",
                        fontWeight = FontWeight.Bold,
                        color = MauXanhNgoc
                    )
                }
            }
        }

        // ========== BÁO CÁO TÀI CHÍNH ==========
        item {
            Text("Báo cáo tài chính", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MauTim)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MauTrang),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                val totalIncome = transactions.filter { it.type.equals("income", ignoreCase = true) }.sumOf { it.amount }
                val totalExpense = transactions.filter { it.type.equals("expense", ignoreCase = true) }.sumOf { it.amount }
                val balance = totalIncome - totalExpense

                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tổng quan", fontWeight = FontWeight.Bold, color = MauTim)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Tổng thu: ${currencyFormatter.format(totalIncome)}", color = MauXanhNgoc)
                    Text("Tổng chi: ${currencyFormatter.format(totalExpense)}", color = MauTim)
                    Text("Số dư: ${currencyFormatter.format(balance)}", fontWeight = FontWeight.Bold,
                        color = if (balance >= 0) MauXanhNgoc else MauHong
                    )
                }
            }
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MauTim),
                    onClick = {
                        try {
                            val file = ExcelExporter.export(context, transactions)
                            Toast.makeText(context, "Đã xuất Excel: ${file.name}", Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Lỗi xuất Excel: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Icon(Icons.Default.TableChart, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Xuất Excel")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MauHong),
                    onClick = {
                        try {
                            val file = PdfExporter.export(context, transactions)
                            Toast.makeText(context, "Đã xuất PDF: ${file.name}", Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Lỗi xuất PDF: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Icon(Icons.Default.PictureAsPdf, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Xuất PDF")
                }
            }
        }

        item {
            Text("Chi tiết giao dịch", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MauTim)
        }

        if (transactions.isEmpty()) {
            item {
                Box(Modifier.fillMaxWidth().padding(20.dp), Alignment.Center) {
                    Text("Chưa có dữ liệu giao dịch nào", color = Color.Gray)
                }
            }
        } else {
            items(transactions) { transaction ->
                TransactionItem(transaction = transaction, formatter = currencyFormatter)
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}