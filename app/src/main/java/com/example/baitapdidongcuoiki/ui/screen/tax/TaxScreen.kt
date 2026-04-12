package com.example.baitapdidongcuoiki.ui.screen.tax

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxScreen(
    viewModel: TaxViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    val animatedTax by animateFloatAsState(
        targetValue = state.tax.toFloat(),
        label = "tax_animation"
    )

    // Gradient nền hồng tím
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8BBD0),
            Color(0xFFE1BEE7),
            Color(0xFFFFFFFF)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Thuế TNCN", color = Color(0xFF4A148C)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White.copy(alpha = 0.9f))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Thuế TNCN (lũy tiến)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A148C)
                )
                Text(
                    "Nhập thu nhập theo nguồn (lương, cho thuê nhà, khác). " +
                            "Áp dụng giảm trừ 11 triệu/tháng + 4,4 triệu/người phụ thuộc.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6A1B9A)
                )

                // Các Card nhập liệu bo góc
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = state.salaryInput,
                            onValueChange = viewModel::onSalaryChange,
                            label = { Text("Thu nhập từ lương / tiền công (VNĐ)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF9C27B0),
                                unfocusedBorderColor = Color(0xFFCE93D8)
                            )
                        )
                        OutlinedTextField(
                            value = state.rentalInput,
                            onValueChange = viewModel::onRentalChange,
                            label = { Text("Thu nhập cho thuê tài sản (VD: nhà) (VNĐ)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF9C27B0),
                                unfocusedBorderColor = Color(0xFFCE93D8)
                            )
                        )
                        OutlinedTextField(
                            value = state.otherIncomeInput,
                            onValueChange = viewModel::onOtherIncomeChange,
                            label = { Text("Thu nhập khác (VNĐ)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF9C27B0),
                                unfocusedBorderColor = Color(0xFFCE93D8)
                            )
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Người phụ thuộc: ${state.dependents}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF4A148C)
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = viewModel::decrementDependents) {
                                    Icon(Icons.Default.Remove, contentDescription = "Giảm", tint = Color(0xFF9C27B0))
                                }
                                IconButton(onClick = viewModel::incrementDependents) {
                                    Icon(Icons.Default.Add, contentDescription = "Tăng", tint = Color(0xFF9C27B0))
                                }
                            }
                        }
                    }
                }

                // Card kết quả bo góc đẹp
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Tổng thu nhập tính thuế", fontWeight = FontWeight.Medium, color = Color(0xFF6A1B9A))
                        Text(formatter.format(state.grossIncome), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A148C))

                        Divider(color = Color(0xFFE1BEE7))
                        Text("Giảm trừ bản thân", fontWeight = FontWeight.Medium, color = Color(0xFF6A1B9A))
                        Text(formatter.format(state.personalDeduction))

                        Divider(color = Color(0xFFE1BEE7))
                        Text("Giảm trừ người phụ thuộc", fontWeight = FontWeight.Medium, color = Color(0xFF6A1B9A))
                        Text(formatter.format(state.dependantDeduction))

                        Divider(color = Color(0xFFE1BEE7))
                        Text("Thu nhập chịu thuế", fontWeight = FontWeight.Medium, color = Color(0xFF6A1B9A))
                        Text(formatter.format(state.taxableIncome))

                        Divider(color = Color(0xFFE1BEE7))
                        Text("Thuế phải nộp (biểu lũy tiến từng phần)", fontWeight = FontWeight.Bold, color = Color(0xFFC2185B))
                        Text(
                            formatter.format(animatedTax.toDouble()),
                            color = Color(0xFFC62828),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            viewModel.settlementSummaryText(),
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(4.dp, RoundedCornerShape(26.dp)),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                ) {
                    Text("Quyết toán thuế (xem tóm tắt)", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}