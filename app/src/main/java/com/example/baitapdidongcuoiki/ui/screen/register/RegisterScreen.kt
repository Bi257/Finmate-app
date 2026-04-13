package com.example.baitapdidongcuoiki.ui.screen.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.baitapdidongcuoiki.ui.screen.login.MyGradient
import com.example.baitapdidongcuoiki.ui.screen.login.MyLightPurple
import com.example.baitapdidongcuoiki.ui.screen.login.MyPurple

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Theo dõi trạng thái đăng ký để hiện Toast và chuyển trang
    LaunchedEffect(viewModel.registerStatus) {
        if (viewModel.registerStatus.contains("thành công")) {
            Toast.makeText(context, viewModel.registerStatus, Toast.LENGTH_SHORT).show()
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        } else if (viewModel.registerStatus.contains("Lỗi")) {
            Toast.makeText(context, viewModel.registerStatus, Toast.LENGTH_LONG).show()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MyLightPurple) {
        Box(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Tạo tài khoản", color = MyPurple, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = MyPurple) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        label = { Text("Mật khẩu") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = MyPurple) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = viewModel.confirmPassword,
                        onValueChange = { viewModel.confirmPassword = it },
                        label = { Text("Xác nhận mật khẩu") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = MyPurple) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nút Đăng ký
                    Box(
                        modifier = Modifier.fillMaxWidth().height(56.dp).background(MyGradient, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { viewModel.onRegisterClick() },
                            modifier = Modifier.fillMaxSize(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text("ĐĂNG KÝ", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Đã có tài khoản? Đăng nhập ngay", color = MyPurple)
                    }
                }
            }
        }
    }
}



























































