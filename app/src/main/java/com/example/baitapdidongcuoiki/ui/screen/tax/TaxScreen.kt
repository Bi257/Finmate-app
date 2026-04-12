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

    // Bảng màu đồng bộ HomeScreen
    val PurpleMain = Color(0xFF9C27B0)
    val SoftPink = Color(0xFFFFE1E6)
    val SoftPurple = Color(0xFFF3E5F5)
    val WhiteCard = Color.White

    val animatedTax by animateFloatAsState(
        targetValue = state.tax.toFloat(),
        label = "tax_animation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SoftPink, SoftPurple, Color.White)
                )
            )
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleMain
            )

            Text(
                "Nhập thu nhập để tính thuế. Áp dụng giảm trừ bản thân 11tr/tháng.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            // Sửa lỗi: Sử dụng OutlinedTextFieldDefaults.outlinedTextFieldColors
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurpleMain,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = PurpleMain
            )

            OutlinedTextField(
                value = state.salaryInput,
                onValueChange = viewModel::onSalaryChange,
                label = { Text("Thu nhập từ lương (VNĐ)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )

            OutlinedTextField(
                value = state.rentalInput,
                onValueChange = viewModel::onRentalChange,
                label = { Text("Thu nhập cho thuê tài sản (VNĐ)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )

            OutlinedTextField(
                value = state.otherIncomeInput,
                onValueChange = viewModel::onOtherIncomeChange,
                label = { Text("Thu nhập khác (VNĐ)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Người phụ thuộc: ${state.dependents}", fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = viewModel::decrementDependents) {
                        Icon(Icons.Default.Remove, "Giảm", tint = PurpleMain)
                    }
                    IconButton(onClick = viewModel::incrementDependents) {
                        Icon(Icons.Default.Add, "Tăng", tint = PurpleMain)
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = WhiteCard),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Chi tiết quyết toán", fontWeight = FontWeight.Bold, color = PurpleMain)
                    Spacer(modifier = Modifier.height(12.dp))

                    TaxInfoRow("Tổng thu nhập", formatter.format(state.grossIncome))
                    TaxInfoRow("Giảm trừ gia cảnh", formatter.format(state.personalDeduction + state.dependantDeduction))

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = SoftPurple)

                    Text("Thuế phải nộp:", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Text(
                        formatter.format(animatedTax.toDouble()),
                        color = Color(0xFFC62828), // Màu đỏ nhấn mạnh số tiền thuế
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Button(
                onClick = {
                    Toast.makeText(context, viewModel.settlementSummaryText(), Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
            ) {
                Text("Xem tóm tắt quyết toán", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TaxInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}