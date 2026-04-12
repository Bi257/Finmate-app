package com.example.baitapdidongcuoiki.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.NumberFormat
import java.util.*
import com.example.baitapdidongcuoiki.ui.components.*
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("vi", "VN")) }

    // Bảng màu Hồng Tím Pastel
    val PurpleMain = Color(0xFF9C27B0)
    val SoftPink = Color(0xFFFFE1E6)
    val WhiteCard = Color.White

    // Nền Gradient 3 màu: Hồng -> Tím Nhạt -> Trắng
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SoftPink,
                        Color(0xFFF3E5F5), // Tím nhạt trung gian
                        Color.White
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 🔹 HEADER
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Xin chào 😊", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PurpleMain)
                        Text("Chào mừng bạn trở lại", color = Color.Gray)
                    }
                    IconButton(onClick = { navController.navigate("profile") }, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Default.AccountCircle, null, modifier = Modifier.size(45.dp), tint = PurpleMain)
                    }
                }
            }

            // 🔹 THẺ VÍ CHÍNH
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = WhiteCard),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Ví của tôi", color = Color.Gray, fontSize = 16.sp)

                        val total = state.transactions.sumOf {
                            if (it.type.equals("income", true)) it.amount else -it.amount
                        }

                        Text(
                            formatter.format(total),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PurpleMain
                        )
                    }
                }
            }

            // 🔹 THU - CHI (Đã cập nhật màu đỏ đậm hơn)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    val income = state.transactions
                        .filter { it.type.equals("income", true) }
                        .sumOf { it.amount }

                    val expense = state.transactions
                        .filter { it.type.equals("expense", true) }
                        .sumOf { it.amount }

                    SummaryCard(
                        label = "Thu",
                        amount = formatter.format(income),
                        icon = Icons.Default.ArrowUpward,
                        bgColor = Color(0xFFE6F4EA),
                        contentColor = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )

                    SummaryCard(
                        label = "Chi",
                        amount = formatter.format(expense),
                        icon = Icons.Default.ArrowDownward,
                        bgColor = Color(0xFFFDECEC),
                        contentColor = Color(0xFFC62828), // MÀU ĐỎ ĐẬM TIÊU CHUẨN
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 🔹 BIỂU ĐỒ
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = WhiteCard),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Biểu đồ thu chi (7 GD gần nhất)", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth().height(150.dp)
                                .background(Color(0xFFF3E5F5).copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Cần ít nhất 2 giao dịch để vẽ biểu đồ",
                                color = Color.LightGray,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // 🔹 DANH SÁCH GIAO DỊCH
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Giao dịch gần đây", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Xem tất cả >", color = PurpleMain, fontSize = 14.sp, modifier = Modifier.clickable { })
                }
            }

            if (state.transactions.isEmpty()) {
                item {
                    Text(
                        "Chưa có dữ liệu",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                items(state.transactions.take(5)) { transaction ->
                    TransactionItem(transaction, formatter)
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SummaryCard(
    label: String,
    amount: String,
    icon: ImageVector,
    bgColor: Color,
    contentColor: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = contentColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, color = contentColor, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                amount,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
                fontSize = 14.sp
            )
        }
    }
}