package com.example.baitapdidongcuoiki.ui.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val userEmail = auth.currentUser?.email ?: "Chưa đăng nhập"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon đại diện
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color(0xFF9C27B0)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Xin chào,",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Text(
            text = userEmail,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(40.dp))

        // NÚT ĐĂNG XUẤT - QUAN TRỌNG NHẤT
        Button(
            onClick = {
                // 1. Thoát khỏi Firebase
                auth.signOut()

                // 2. Điều hướng về Login và xóa sạch bộ nhớ đệm (ViewModel)
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ĐĂNG XUẤT",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}