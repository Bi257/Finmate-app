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

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    val balance = transactions.sumOf {
        if (it.type == "income") it.amount else -it.amount
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA)), // nền xám nhẹ
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // ===== HEADER =====
        item {
            Text(
                "Ví của tôi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // ===== CARD SỐ DƯ (FIX FULL WIDTH) =====
        item {
            Card(
                modifier = Modifier.fillMaxWidth(), // 🔥 FIX LỆCH
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF00C6FF), Color(0xFF0072FF))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {

                        Text(
                            "Số dư ví",
                            color = Color.White.copy(0.8f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            formatter.format(balance),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 🔥 BUTTON ĐẸP HƠN
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
                                tint = Color(0xFF0072FF)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Rút về ngân hàng",
                                color = Color(0xFF0072FF),
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
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                WalletAction("Cố định", Icons.Default.Star)
                WalletAction("Khác", Icons.Default.MoreHoriz)
            }
        }

        item {
            OutlinedButton(
                onClick = { navController.navigate("report") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Description, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Báo cáo Excel / PDF")
            }
        }

        // ===== LỊCH SỬ =====
        item {
            Text(
                "Lịch sử biến động",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (transactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
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

// ===== ACTION ITEM ĐẸP HƠN =====
@Composable
fun WalletAction(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF0072FF))
        Text(title, fontWeight = FontWeight.Medium)
    }
}