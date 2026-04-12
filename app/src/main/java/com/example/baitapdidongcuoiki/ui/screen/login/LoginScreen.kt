package com.example.baitapdidongcuoiki.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
// Thêm 2 dòng này vào nhóm các dòng import đầu file
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

// Bảng màu đồng bộ
val MyPurple = Color(0xFF9C27B0)
val MyPink = Color(0xFFE91E63)
val MyLightPurple = Color(0xFFF3E5F5)
val MyGradient = Brush.linearGradient(colors = listOf(Color(0xFFE91E63), Color(0xFF9C27B0)))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    // Surface bao phủ toàn bộ để xóa vạch trắng hệ thống
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MyLightPurple
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo & Brand
                Box(
                    modifier = Modifier
                        .size(120.dp), // Tăng kích thước lên một chút để thấy rõ khung kim loại
                    contentAlignment = Alignment.Center
                ) {
                    Image(

                        painter = painterResource(id = com.example.baitapdidongcuoiki.R.drawable.login),
                        contentDescription = "FinMate",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "FinMate", color = MyPurple.copy(alpha = 0.8f), fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)

                Spacer(modifier = Modifier.height(40.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Đăng nhập", color = MyPurple, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = viewModel.username,
                            onValueChange = { viewModel.username = it },
                            placeholder = { Text("Tên tài khoản") },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = MyPurple.copy(0.4f)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MyPurple, unfocusedBorderColor = MyLightPurple)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = viewModel.password,
                            onValueChange = { viewModel.password = it },
                            placeholder = { Text("Mật khẩu") },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = MyPurple.copy(0.4f)) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MyPurple, unfocusedBorderColor = MyLightPurple)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                viewModel.onLoginClick()
                                if (viewModel.loginStatus.contains("thành công")) {
                                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp).background(MyGradient, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text("ĐĂNG NHẬP", color = Color.White, fontWeight = FontWeight.ExtraBold)
                        }

                        // Nút chuyển sang Đăng ký
                        TextButton(onClick = { navController.navigate("register") }) {
                            Text("Chưa có tài khoản? Đăng ký ngay", color = MyPurple)
                        }
                    }
                }
            }
        }
    }
}