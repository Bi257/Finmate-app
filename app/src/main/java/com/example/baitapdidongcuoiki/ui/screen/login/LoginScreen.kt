package com.example.baitapdidongcuoiki.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // Thêm dòng này
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // Thêm dòng này
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baitapdidongcuoiki.R

val MyPurple = Color(0xFF9C27B0)
val MyPink = Color(0xFFE91E63)
val MyLightPurple = Color(0xFFF3E5F5)
val MyGradient = Brush.linearGradient(colors = listOf(Color(0xFFE91E63), Color(0xFF9C27B0)))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {

    LaunchedEffect(viewModel.loginStatus) {
        if (viewModel.loginStatus.contains("thành công", ignoreCase = true)) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MyLightPurple
    ) {
        // GIẢI PHÁP: Thêm verticalScroll để có thể vuốt lên thấy nút đăng ký
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = "FinMate",
                    modifier = Modifier.size(100.dp) // Giảm size logo một chút cho đỡ chật
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "FinMate",
                    color = MyPurple.copy(alpha = 0.8f),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text("TEST GIT 123", color = Color.Red)

                Spacer(modifier = Modifier.height(24.dp)) // Giảm khoảng cách này xuống

                Text(text = "FinMate", color = MyPurple.copy(alpha = 0.8f), fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(40.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp), // Giảm padding card cho gọn
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Đăng nhập",
                            color = MyPurple,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = viewModel.username,
                            onValueChange = { viewModel.username = it },
                            placeholder = { Text("Email") },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = MyPurple.copy(0.4f)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.password,
                            onValueChange = { viewModel.password = it },
                            placeholder = { Text("Mật khẩu") },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = MyPurple.copy(0.4f)) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Nút Đăng nhập - Sửa lại cách bọc background để luôn hiện màu
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(MyGradient, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = { viewModel.onLoginClick() },
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text("ĐĂNG NHẬP", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Nút chuyển sang Đăng ký
                        TextButton(
                            onClick = { navController.navigate("register") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row {
                                Text("Chưa có tài khoản? ", color = Color.Gray)
                                Text("Đăng ký ngay", color = MyPurple, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}