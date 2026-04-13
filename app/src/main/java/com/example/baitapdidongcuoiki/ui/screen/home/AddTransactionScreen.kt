package com.example.baitapdidongcuoiki.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("income") }

    // Bảng màu đồng bộ giao diện
    val PurpleMain = Color(0xFF9C27B0)
    val SoftPink = Color(0xFFFFE1E6)
    val SoftPurple = Color(0xFFF3E5F5)
    val WhiteCard = Color.White

    // Cấu hình màu sắc cho OutlinedTextField để sửa lỗi tham chiếu
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = PurpleMain,
        unfocusedBorderColor = Color.LightGray,
        focusedLabelColor = PurpleMain,
        cursorColor = PurpleMain
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(SoftPink, SoftPurple, Color.White)))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Thêm giao dịch", fontWeight = FontWeight.Bold, color = PurpleMain) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            // Sử dụng AutoMirrored để sửa cảnh báo deprecated
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại", tint = PurpleMain)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = WhiteCard.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Chi tiết giao dịch", fontWeight = FontWeight.Bold, color = Color.Gray)

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Tên giao dịch (VD: Ăn sáng)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = textFieldColors
                        )

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it.filter { c -> c.isDigit() } },
                            label = { Text("Số tiền (VNĐ)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = textFieldColors
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SelectableChip(
                        selected = type == "income",
                        onClick = { type = "income" },
                        label = "Thu nhập",
                        selectedColor = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    SelectableChip(
                        selected = type == "expense",
                        onClick = { type = "expense" },
                        label = "Chi tiêu",
                        selectedColor = Color(0xFFD32F2F),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        val amountValue = amount.toDoubleOrNull()
                        if (title.isBlank()) return@Button
                        if (amountValue == null || amountValue <= 0) return@Button

                        viewModel.addTransaction(title = title, amount = amountValue, type = type)
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
                ) {
                    Text("Lưu giao dịch", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SelectableChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    selectedColor: Color,
    modifier: Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (selected) selectedColor.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) selectedColor else Color.LightGray.copy(alpha = 0.5f)
        ),
        modifier = modifier.height(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if (selected) selectedColor else Color.Gray,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}







































































