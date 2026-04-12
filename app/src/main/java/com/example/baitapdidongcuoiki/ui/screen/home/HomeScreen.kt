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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.baitapdidongcuoiki.ui.components.TransactionItem
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.graphics.drawscope.Stroke

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
                                color = Color(0xFF4A148C) // tím đậm
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

                // Biểu đồ thu chi (7 GD gần nhất)
                // Biểu đồ thu chi dạng đường cong (parabol) - 2 màu riêng
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
                                text = "Biểu đồ thu chi dạng đường cong",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6A1B9A)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Lấy 10 giao dịch gần nhất (để đường cong mượt hơn)
                            val recentTransactions = transactions
                                .sortedByDescending { it.date }
                                .take(10)
                                .reversed()

                            // Tách riêng thu và chi, sắp xếp theo thứ tự thời gian tăng dần
                            val incomeTransactions = recentTransactions
                                .filter { it.type.equals("income", ignoreCase = true) }
                                .sortedBy { it.date }
                            val expenseTransactions = recentTransactions
                                .filter { it.type.equals("expense", ignoreCase = true) }
                                .sortedBy { it.date }

                            if (incomeTransactions.size < 2 && expenseTransactions.size < 2) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .background(Color(0xFFF3E5F5), RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Cần ít nhất 2 giao dịch (thu hoặc chi) để vẽ biểu đồ",
                                        color = Color(0xFF8E24AA),
                                        fontSize = 14.sp,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            } else {
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                ) {
                                    val paddingTop = 20f
                                    val paddingBottom = 20f
                                    val availableHeight = size.height - paddingTop - paddingBottom
                                    val maxValue = maxOf(
                                        incomeTransactions.maxOfOrNull { it.amount } ?: 0.0,
                                        expenseTransactions.maxOfOrNull { it.amount } ?: 0.0
                                    ).toFloat()

                                    // Hàm vẽ đường cong nội suy parabol qua các điểm
                                    fun drawCurve(
                                        points: List<Pair<Float, Float>>,
                                        color: Color,
                                        strokeWidth: Float = 4f
                                    ) {
                                        if (points.size < 2) return
                                        // Tìm hàm bậc 2 (parabol) đi qua 3 điểm cuối (hoặc toàn bộ nếu <3)
                                        // Đơn giản: nối các điểm bằng đường cong Bézier (cubic)
                                        // Chúng ta sẽ dùng Path với đường cong cubic qua từng cặp điểm
                                        val path = androidx.compose.ui.graphics.Path().apply {
                                            moveTo(points.first().first, points.first().second)
                                            for (i in 0 until points.size - 1) {
                                                val p0 = points[i]
                                                val p1 = points[i + 1]
                                                val cp1x = p0.first + (p1.first - p0.first) * 0.33f
                                                val cp1y = p0.second
                                                val cp2x = p1.first - (p1.first - p0.first) * 0.33f
                                                val cp2y = p1.second
                                                cubicTo(cp1x, cp1y, cp2x, cp2y, p1.first, p1.second)
                                            }
                                        }
                                        drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
                                    }

                                    // Vẽ lưới và trục (tuỳ chọn)
                                    // Vẽ trục X (đường ngang) và Y (đường dọc) nhạt
                                    drawLine(
                                        color = Color.LightGray,
                                        start = Offset(0f, size.height - paddingBottom),
                                        end = Offset(size.width, size.height - paddingBottom),
                                        strokeWidth = 1f
                                    )
                                    drawLine(
                                        color = Color.LightGray,
                                        start = Offset(0f, paddingTop),
                                        end = Offset(0f, size.height - paddingBottom),
                                        strokeWidth = 1f
                                    )

                                    // Hàm chuyển đổi giá trị tiền thành tọa độ Y
                                    fun valueToY(value: Float): Float {
                                        if (maxValue == 0f) return size.height - paddingBottom
                                        return paddingTop + availableHeight * (1f - (value / maxValue))
                                    }

                                    // Tạo danh sách điểm cho thu và chi
                                    val incomePoints = incomeTransactions.mapIndexed { index, tx ->
                                        val x = (index.toFloat() / (incomeTransactions.size - 1).coerceAtLeast(1)) * size.width
                                        val y = valueToY(tx.amount.toFloat())
                                        x to y
                                    }
                                    val expensePoints = expenseTransactions.mapIndexed { index, tx ->
                                        val x = (index.toFloat() / (expenseTransactions.size - 1).coerceAtLeast(1)) * size.width
                                        val y = valueToY(tx.amount.toFloat())
                                        x to y
                                    }

                                    // Vẽ đường cong cho thu (xanh)
                                    drawCurve(incomePoints, Color(0xFF4CAF50), 4f)
                                    // Vẽ đường cong cho chi (đỏ)
                                    drawCurve(expensePoints, Color(0xFFF44336), 4f)

                                    // Vẽ các điểm tròn tại vị trí giao dịch (tuỳ chọn)
                                    incomePoints.forEach { point ->
                                        drawCircle(
                                            color = Color(0xFF4CAF50),
                                            radius = 6f,
                                            center = Offset(point.first, point.second)
                                        )
                                    }
                                    expensePoints.forEach { point ->
                                        drawCircle(
                                            color = Color(0xFFF44336),
                                            radius = 6f,
                                            center = Offset(point.first, point.second)
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
                                    Spacer(Modifier.width(16.dp))
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