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

    // --- State cho Dialog Thuế ---
    var showTaxDialog by remember { mutableStateOf(false) }
    var dependentCount by remember { mutableStateOf("0") }

    // --- Định nghĩa bảng màu ---
    val MauTrang = Color.White
    val MauTim = Color(0xFF9C27B0)
    val MauHong = Color(0xFFE91E63)
    val NenNhat = Color(0xFFF3E5F5)

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("vi", "VN")) }
    val numberFormatter = remember { NumberFormat.getNumberInstance(Locale.US) }
    val currentTime = remember { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) }

    // --- Logic Quy đổi nhanh ---
    var inputAmount by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("USD") }
    var expanded by remember { mutableStateOf(false) }
    val currencyOptions = listOf("USD", "EUR", "JPY", "Vàng (PAXG/lượng)")

    // Lấy tỷ giá từ state
    val usdRate = marketState.rates.find { it.currency == "USD" }?.buy ?: 0.0
    val eurRate = marketState.rates.find { it.currency == "EUR" }?.buy ?: 0.0
    val jpyRate = marketState.rates.find { it.currency == "JPY" }?.buy ?: 0.0
    val goldPrice = marketState.goldPricePerLuongVnd

    // Tính toán kết quả quy đổi tự động
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ========== PHẦN 1: THỊ TRƯỜNG & TỶ GIÁ ==========
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
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tỷ giá & Giá vàng", fontWeight = FontWeight.Bold, color = MauTim)
                    Spacer(modifier = Modifier.height(12.dp))

                    marketState.rates.take(3).forEach { rate ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${rate.currency}/VND", modifier = Modifier.weight(1f))
                            Text(numberFormatter.format(rate.buy), textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                            Text("${rate.changePercent}%", color = if(rate.changePercent>=0) Color(0xFF4CAF50) else MauHong, textAlign = TextAlign.End, modifier = Modifier.weight(0.5f))
                        }
                    }

                    if (goldPrice > 0) {
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        Text("Vàng: ${numberFormatter.format(goldPrice)} VND", fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Quy đổi nhanh", fontWeight = FontWeight.Bold, color = MauTim, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = inputAmount,
                            onValueChange = { inputAmount = it },
                            label = { Text("Số lượng") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Box(Modifier.weight(0.8f)) {
                            Button(onClick = { expanded = true }, colors = ButtonDefaults.buttonColors(containerColor = MauTim)) {
                                Text(selectedCurrency, color = MauTrang, fontSize = 11.sp, maxLines = 1)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                currencyOptions.forEach { option ->
                                    DropdownMenuItem(text = { Text(option) }, onClick = { selectedCurrency = option; expanded = false })
                                }
                            }
                        }
                    }

                    // HIỂN THỊ KẾT QUẢ QUY ĐỔI SAU KHI NHẬP TIỀN
                    if (inputAmount.isNotEmpty() && conversionResult > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MauTim.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.SyncAlt, contentDescription = null, tint = MauTim, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "= ${currencyFormatter.format(conversionResult)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MauTim
                                )
                            }
                        }
                    }
                }
            }
        }

        // ========== PHẦN 2: XUẤT FILE BÁO CÁO ==========
        item {
            Text("Xuất báo cáo tài chính", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MauTim)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // 1. Báo cáo Thu chi (Excel)
                ExportCard(
                    title = "Báo cáo Thu chi",
                    description = "Chi tiết tất cả giao dịch (File Excel)",
                    icon = Icons.Default.TableChart,
                    color = Color(0xFF4CAF50),
                    onClick = {
                        try {
                            ExcelExporter.export(context, transactions)
                            Toast.makeText(context, "Đã xuất Excel thành công", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) { Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show() }
                    }
                )

                // 2. Quyết toán Thuế TNCN (PDF)
                ExportCard(
                    title = "Quyết toán Thuế TNCN",
                    description = "Dự toán thuế & Giảm trừ gia cảnh (PDF)",
                    icon = Icons.Default.Description,
                    color = MauTim,
                    onClick = { showTaxDialog = true }
                )

                // 3. Phân tích biến động (PDF)
                ExportCard(
                    title = "Phân tích biến động",
                    description = "Biểu đồ xu hướng và tỷ lệ (PDF)",
                    icon = Icons.Default.PieChart,
                    color = Color(0xFF2196F3),
                    onClick = {
                        try {
                            PdfExporter.export(
                                context = context,
                                transactions = transactions,
                                type = PdfExporter.ReportType.ANALYSIS
                            )
                            Toast.makeText(context, "Đã xuất PDF Biến động thành công", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) { Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show() }
                    }
                )
            }
        }

        // Đã xóa phần "Chi tiết gần đây" tại đây để màn hình gọn gàng hơn

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }

    // --- DIALOG NHẬP NGƯỜI PHỤ THUỘC ---
    if (showTaxDialog) {
        AlertDialog(
            onDismissRequest = { showTaxDialog = false },
            title = { Text("Cấu hình Thuế TNCN", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Nhập số người phụ thuộc (giảm trừ 4.4tr/người):", fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = dependentCount,
                        onValueChange = { if (it.all { char -> char.isDigit() }) dependentCount = it },
                        label = { Text("Số người") },
                        modifier = Modifier.fillMaxWidth(),
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
                            Toast.makeText(context, "Đã xuất báo cáo thuế!", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MauTim)
                ) { Text("Xuất PDF") }
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(color.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = description, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}