package com.example.baitapdidongcuoiki.ui.screen.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baitapdidongcuoiki.ui.components.TransactionItem
import com.example.baitapdidongcuoiki.domain.model.Transaction
import java.text.NumberFormat
import java.util.*

@Composable
fun WalletScreen(
    transactions: List<Transaction>,
    navController: NavController
) {
    // Bảng màu đồng bộ HomeScreen
    val PurpleMain = Color(0xFF9C27B0)
    val SoftPink = Color(0xFFFFE1E6)
    val SoftPurple = Color(0xFFF3E5F5)
    val WhiteCard = Color.White

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    val balance = transactions.sumOf {
        if (it.type == "income") it.amount else -it.amount
    }

    // Nền Gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SoftPink, SoftPurple, Color.White)
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ===== HEADER =====
            item {
                Text(
                    "Ví của tôi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleMain
                )
            }

            // ===== CARD SỐ DƯ (MÀU TÍM HỒNG CHỦ ĐẠO) =====
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(PurpleMain, Color(0xFFE91E63)) // Tím sang Hồng đậm
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                "Số dư ví",
                                color = Color.White.copy(0.8f),
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                formatter.format(balance),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { /* TODO */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 10.dp
                                )
                            ) {
                                Icon(
                                    Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = PurpleMain
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Rút về ngân hàng",
                                    color = PurpleMain,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // ===== QUICK ACTION =====
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Truyền bảng màu vào component con
                    WalletAction("Cố định", Icons.Default.Star, PurpleMain, WhiteCard, Modifier.weight(1f))
                    WalletAction("Khác", Icons.Default.MoreHoriz, PurpleMain, WhiteCard, Modifier.weight(1f))
                }
            }

            item {
                OutlinedButton(
                    onClick = { navController.navigate("report") },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PurpleMain)
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = PurpleMain)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Báo cáo Excel / PDF", color = PurpleMain, fontWeight = FontWeight.Bold)
                }
            }

            // ===== LỊCH SỬ =====
            item {
                Text(
                    "Lịch sử biến động",
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (transactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Chưa có dữ liệu", color = Color.Gray)
                    }
                }
            } else {
                items(transactions) {
                    TransactionItem(it, formatter)
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

// ===== ACTION ITEM ĐỒNG BỘ MÀU =====
@Composable
fun WalletAction(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(
                bgColor.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = accentColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontWeight = FontWeight.Bold, color = Color.DarkGray, fontSize = 14.sp)
    }
}