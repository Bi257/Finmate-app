package com.example.baitapdidongcuoiki.ui.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel // 🔹 Thêm cái này
import androidx.navigation.NavController
import kotlinx.coroutines.launch // 🔹 Thêm cái này

// ✨ Sử dụng lại bảng màu Hồng - Tím của bạn
val MyPurple = Color(0xFF9C27B0)
val MyPink = Color(0xFFE91E63)
val MyLightPurple = Color(0xFFF3E5F5)
val MyGradient = Brush.linearGradient(colors = listOf(Color(0xFFE91E63), Color(0xFF9C27B0)))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel() // 🔹 Nhúng ViewModel vào đây
) {
    // Các biến tạm thời để lưu dữ liệu nhập vào
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 🔹 Thêm các biến quản lý trạng thái hiển thị thông báo/loading
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, // 🔹 Hiển thị lỗi nếu có
        containerColor = MyLightPurple
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MyLightPurple
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tạo tài khoản",
                    color = MyPurple,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = "Tham gia cùng FinMate ngay", color = Color.Gray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Ô nhập Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            leadingIcon = { Icon(Icons.Default.Email, null, tint = MyPurple.copy(0.4f)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MyPurple)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Ô nhập Username
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Tên tài khoản") },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = MyPurple.copy(0.4f)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MyPurple)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Ô nhập Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Mật khẩu") },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = MyPurple.copy(0.4f)) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MyPurple)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Nút Đăng ký
                        Button(
                            onClick = {
                                // 🔹 Gọi logic đăng ký từ ViewModel
                                isLoading = true
                                viewModel.register(
                                    email = email,
                                    pass = password,
                                    onSuccess = {
                                        isLoading = false
                                        // Đăng ký thành công thì chuyển màn hình
                                        navController.navigate("home") {
                                            popUpTo("register") { inclusive = true }
                                        }
                                    },
                                    onError = { error ->
                                        isLoading = false
                                        scope.launch {
                                            snackbarHostState.showSnackbar(error)
                                        }
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(MyGradient, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            enabled = !isLoading // 🔹 Vô hiệu hóa nút khi đang load
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("ĐĂNG KÝ", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Nút quay lại Đăng nhập
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Đã có tài khoản? Đăng nhập ngay", color = MyPurple)
                }
            }
        }
    }
}