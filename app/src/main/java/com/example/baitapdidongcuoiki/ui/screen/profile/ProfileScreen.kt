package com.example.baitapdidongcuoiki.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
    val MyPurple = Color(0xFF9C27B0)
    val MyLightPurple = Color(0xFFF3E5F5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MyLightPurple)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Icon người dùng
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(50.dp),
            color = MyPurple
        ) {
            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(20.dp))
        }

        Text(
            "Admin",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp),
            color = MyPurple
        )

        Spacer(modifier = Modifier.weight(1f)) // Đẩy nút Đăng xuất xuống đáy

        // 🔥 THANH ĐĂNG XUẤT
        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo(0) // Xóa sạch lịch sử để không quay lại được trang chủ
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFEBEE), // Màu hồng đỏ nhạt
                contentColor = Color.Red
            )
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ĐĂNG XUẤT", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(40.dp)) // Cách thanh điều hướng một khoảng
    }
}