package com.example.baitapdidongcuoiki.ui.screen.report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.baitapdidongcuoiki.export.ExcelExporter
import com.example.baitapdidongcuoiki.export.PdfExporter
import com.example.baitapdidongcuoiki.ui.DailyMarketViewModel
import com.example.baitapdidongcuoiki.ui.screen.home.HomeViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    homeViewModel: HomeViewModel,
    marketViewModel: DailyMarketViewModel
) {
    val context = LocalContext.current
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val marketState by marketViewModel.ui.collectAsStateWithLifecycle()
    val transactions = homeState.transactions

    // Logic tính toán tổng quan
    val totalIncome = transactions.filter { it.type.equals("income", true) }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type.equals("expense", true) }.sumOf { it.amount }
    val balance = totalIncome - totalExpense

    // State cho Dialog Thuế
    var showTaxDialog by remember { mutableStateOf(false) }
    var dependentCount by remember { mutableStateOf("0") }


    val MauTrang = Color.White
    val MauTim = Color(0xFF9C27B0)
    val MauHong = Color(0xFFE91E63)
    val MauXanh = Color(0xFF4CAF50)
    val MauVang = Color(0xFFFFC107)
    val NenNhat = Color(0xFFF3E5F5)

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("vi", "VN")) }
    val numberFormatter = remember { NumberFormat.getNumberInstance(Locale.US) }
    val currentTime = remember { SimpleDateFormat("HH:mm, dd/MM", Locale.getDefault()).format(Date()) }

    var inputAmount by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("USD") }
    var expanded by remember { mutableStateOf(false) }
    val currencyOptions = listOf("USD", "EUR", "JPY", "Vàng (PAXG/lượng)")

    // Tỷ giá
    val usdRate = marketState.rates.find { it.currency == "USD" }?.buy ?: 0.0
    val eurRate = marketState.rates.find { it.currency == "EUR" }?.buy ?: 0.0
    val jpyRate = marketState.rates.find { it.currency == "JPY" }?.buy ?: 0.0
    val goldPrice = marketState.goldPricePerLuongVnd

    val conversionResult = remember(inputAmount, selectedCurrency, marketState) {
        val amount = inputAmount.toDoubleOrNull() ?: 0.0
        val rate = when (selectedCurrency) {
            "USD" -> usdRate
            "EUR" -> eurRate
            "JPY" -> jpyRate
            "Vàng (PAXG/lượng)" -> goldPrice
            else -> 0.0
        }
        amount * rate
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(NenNhat),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Trung tâm báo cáo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MauTim)
                    Text("Cập nhật cuối: $currentTime", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Icon(Icons.Default.Analytics, contentDescription = null, tint = MauTim, modifier = Modifier.size(32.dp))
            }
        }

        // TỔNG QUAN TÀI CHÍNH
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MauTim),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Số dư thực tế", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text(currencyFormatter.format(balance), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color.Green, modifier = Modifier.size(16.dp))
                                Text(" Thu nhập", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                            Text(currencyFormatter.format(totalIncome), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = MauHong, modifier = Modifier.size(16.dp))
                                Text(" Chi tiêu", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                            Text(currencyFormatter.format(totalExpense), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // THỊ TRƯỜNG & QUY ĐỔI
        item {
            Text("Công cụ thị trường", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MauTrang),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tỷ giá hối đoái", fontWeight = FontWeight.Bold, color = MauTim)
                    Spacer(modifier = Modifier.height(12.dp))

                    marketState.rates.take(3).forEach { rate ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${rate.currency}/VND", fontWeight = FontWeight.Medium)
                            Text(numberFormatter.format(rate.buy), textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                            Text(" ${if(rate.changePercent>=0) "+" else ""}${rate.changePercent}%",
                                color = if(rate.changePercent>=0) MauXanh else MauHong,
                                textAlign = TextAlign.End,
                                modifier = Modifier.width(60.dp),
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (goldPrice > 0) {
                        HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Vàng SJC (lượng)", fontWeight = FontWeight.Medium)
                            Text(currencyFormatter.format(goldPrice), color = MauVang, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Quy đổi nhanh", fontWeight = FontWeight.Bold, color = MauTim, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = inputAmount,
                            onValueChange = { inputAmount = it },
                            label = { Text("Số lượng") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Box(Modifier.weight(0.8f)) {
                            Button(
                                onClick = { expanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MauTim),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(selectedCurrency, color = MauTrang, fontSize = 11.sp, maxLines = 1)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                currencyOptions.forEach { option ->
                                    DropdownMenuItem(text = { Text(option) }, onClick = { selectedCurrency = option; expanded = false })
                                }
                            }
                        }
                    }

                    if (inputAmount.isNotEmpty() && conversionResult > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MauTim.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CurrencyExchange, contentDescription = null, tint = MauTim, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Ước tính: ${currencyFormatter.format(conversionResult)}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = MauTim
                                )
                            }
                        }
                    }
                }
            }
        }

        // XUẤT BÁO CÁO TÀI CHÍNH
        item {
            Text("Xuất dữ liệu hệ thống", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExportCard(
                    title = "Báo cáo Thu chi Tổng thể",
                    description = "Kết xuất file .xlsx đầy đủ lịch sử giao dịch",
                    icon = Icons.Default.TableChart,
                    color = MauXanh,
                    onClick = {
                        try {
                            ExcelExporter.export(context, transactions)
                            Toast.makeText(context, "✅ Đã lưu Excel vào thư mục Documents", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) { Toast.makeText(context, "❌ Lỗi: ${e.message}", Toast.LENGTH_SHORT).show() }
                    }
                )

                ExportCard(
                    title = "Quyết toán Thuế dự kiến",
                    description = "Tính toán thuế TNCN & Giảm trừ (PDF)",
                    icon = Icons.Default.Assessment,
                    color = MauTim,
                    onClick = { showTaxDialog = true }
                )

                ExportCard(
                    title = "Biến động & Xu hướng",
                    description = "Phân tích biểu đồ và tỷ lệ chi tiêu (PDF)",
                    icon = Icons.Default.Timeline,
                    color = Color(0xFF2196F3),
                    onClick = {
                        try {
                            PdfExporter.export(context, transactions, type = PdfExporter.ReportType.ANALYSIS)
                            Toast.makeText(context, "✅ Đã khởi tạo PDF phân tích", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) { Toast.makeText(context, "❌ Lỗi: ${e.message}", Toast.LENGTH_SHORT).show() }
                    }
                )
            }
        }

        // LƯU Ý HỆ THỐNG
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MauVang.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MauVang.copy(alpha = 0.3f))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MauVang)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Thông tin Thuế 2024", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            "Mức giảm trừ gia cảnh hiện tại là 11 triệu đồng/tháng cho bản thân và 4.4 triệu đồng/tháng cho mỗi người phụ thuộc.",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(120.dp)) }
    }

    //DIALOG THUẾ
    if (showTaxDialog) {
        AlertDialog(
            onDismissRequest = { showTaxDialog = false },
            title = { Text("Cấu hình báo cáo thuế", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Hệ thống sẽ dựa trên tổng thu nhập để tính thuế thu nhập cá nhân dự kiến.", fontSize = 13.sp)
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = dependentCount,
                        onValueChange = { if (it.all { char -> char.isDigit() }) dependentCount = it },
                        label = { Text("Số người phụ thuộc") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showTaxDialog = false
                        try {
                            PdfExporter.export(
                                context = context,
                                transactions = transactions,
                                dependentCount = dependentCount.toIntOrNull() ?: 0,
                                type = PdfExporter.ReportType.TAX
                            )
                            Toast.makeText(context, "📂 Báo cáo PDF đã được tạo thành công", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) { Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show() }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MauTim)
                ) { Text("Xuất file PDF") }
            },
            dismissButton = { TextButton(onClick = { showTaxDialog = false }) { Text("Hủy") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(52.dp).background(color.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(text = description, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}