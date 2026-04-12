package com.example.baitapdidongcuoiki.ui.screen.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.baitapdidongcuoiki.ui.components.TransactionItem
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val transactions = state.transactions
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("vi", "VN")) }

    // Gradient nền hồng tím
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8BBD0), // hồng nhạt
            Color(0xFFE1BEE7), // tím nhạt
            Color(0xFFFFFFFF)  // trắng
        ),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Xin chào 😊",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A148C)
                            )
                            Text(
                                text = "Chào mừng bạn trở lại",
                                fontSize = 14.sp,
                                color = Color(0xFF6A1B9A)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar",
                            tint = Color(0xFF9C27B0),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Card số dư
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .shadow(8.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Ví của tôi",
                                fontSize = 16.sp,
                                color = Color(0xFF6A1B9A),
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = formatter.format(state.balance),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A148C)
                            )
                        }
                    }
                }

                // Thu - Chi
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(4.dp, RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = "Thu",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Thu", fontWeight = FontWeight.Medium, color = Color(0xFF2E7D32))
                                Text(
                                    text = formatter.format(state.totalIncome),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(4.dp, RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = "Chi",
                                    tint = Color(0xFFF44336),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Chi", fontWeight = FontWeight.Medium, color = Color(0xFFC62828))
                                Text(
                                    text = formatter.format(state.totalExpense),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFFB71C1C)
                                )
                            }
                        }
                    }
                }

                // Biểu đồ thu chi (7 GD gần nhất) - Dạng đường cong mượt
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .shadow(6.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Biểu đồ thu chi (7 GD gần nhất)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6A1B9A)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val recentTransactions = transactions
                                .sortedByDescending { it.date }
                                .take(7)
                                .reversed()

                            if (recentTransactions.size < 2) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .background(Color(0xFFF3E5F5), RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Cần ít nhất 2 giao dịch để vẽ biểu đồ",
                                        color = Color(0xFF8E24AA),
                                        fontSize = 14.sp
                                    )
                                }
                            } else {
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                ) {
                                    val values = recentTransactions.map { it.amount.toFloat() }
                                    val maxValue = values.maxOrNull() ?: 1f
                                    val count = values.size
                                    val stepX = size.width / (count - 1)
                                    val topPadding = 30f
                                    val bottomPadding = 20f
                                    val chartHeight = size.height - topPadding - bottomPadding

                                    // Vẽ lưới ngang
                                    for (i in 0..4) {
                                        val y = topPadding + (i * chartHeight / 4)
                                        drawLine(
                                            color = Color(0xFFE0E0E0),
                                            start = Offset(0f, y),
                                            end = Offset(size.width, y),
                                            strokeWidth = 1f
                                        )
                                    }

                                    // Tạo các điểm dữ liệu
                                    val points = mutableListOf<Offset>()
                                    for (i in 0 until count) {
                                        val x = i * stepX
                                        val y = topPadding + chartHeight * (1 - (values[i] / maxValue))
                                        points.add(Offset(x, y))
                                    }

                                    // Vẽ đường cong nối các điểm (cubic bezier)
                                    val path = Path()
                                    if (points.isNotEmpty()) {
                                        path.moveTo(points[0].x, points[0].y)
                                        for (i in 0 until points.size - 1) {
                                            val p0 = points[i]
                                            val p1 = points[i + 1]
                                            val cp1x = p0.x + (p1.x - p0.x) / 2
                                            val cp1y = p0.y
                                            val cp2x = p1.x - (p1.x - p0.x) / 2
                                            val cp2y = p1.y
                                            path.cubicTo(cp1x, cp1y, cp2x, cp2y, p1.x, p1.y)
                                        }
                                    }
                                    drawPath(
                                        path = path,
                                        color = Color(0xFF9C27B0),
                                        style = Stroke(width = 2.5f)
                                    )

                                    // Vẽ các điểm tròn
                                    points.forEachIndexed { index, point ->
                                        val pointColor = if (recentTransactions[index].type.equals("income", ignoreCase = true))
                                            Color(0xFF4CAF50) else Color(0xFFF44336)
                                        drawCircle(
                                            color = Color.White,
                                            radius = 7f,
                                            center = point
                                        )
                                        drawCircle(
                                            color = pointColor,
                                            radius = 5f,
                                            center = point
                                        )
                                    }
                                }
                                // Chú thích
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(Modifier.size(12.dp).background(Color(0xFF4CAF50), RoundedCornerShape(4.dp)))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Thu", fontSize = 12.sp, color = Color(0xFF4CAF50))
                                    Spacer(Modifier.width(12.dp))
                                    Box(Modifier.size(12.dp).background(Color(0xFFF44336), RoundedCornerShape(4.dp)))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Chi", fontSize = 12.sp, color = Color(0xFFF44336))
                                }
                            }
                        }
                    }
                }

                // Giao dịch gần đây
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Giao dịch gần đây",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A148C)
                        )
                        TextButton(
                            onClick = { navController.navigate("report") }
                        ) {
                            Text("Xem tất cả", color = Color(0xFF9C27B0))
                            Icon(Icons.Default.ChevronRight, null, modifier = Modifier.size(18.dp), tint = Color(0xFF9C27B0))
                        }
                    }
                }

                if (transactions.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .shadow(4.dp, RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = "No transaction",
                                    tint = Color(0xFFCE93D8),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Chưa có dữ liệu giao dịch",
                                    color = Color(0xFF8E24AA),
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Hãy thêm giao dịch mới bên dưới",
                                    color = Color(0xFFBA68C8),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                } else {
                    items(transactions.take(5)) { transaction ->
                        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                            TransactionItem(
                                transaction = transaction,
                                formatter = formatter
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}