package com.example.baitapdidongcuoiki.ui.screen.notification

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.baitapdidongcuoiki.ui.components.NotificationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen() {
    val context = LocalContext.current
    val viewModel: NotificationViewModel = hiltViewModel()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    // Bảng màu đồng bộ với HomeScreen
    val PurpleMain = Color(0xFF9C27B0)
    val SoftPink = Color(0xFFFFE1E6)
    val SoftPurple = Color(0xFFF3E5F5)
    val WhiteCard = Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Thông báo",
                        fontWeight = FontWeight.Bold,
                        color = PurpleMain,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SoftPink // Màu nền TopBar hồng nhạt
                )
            )
        }
    ) { padding ->
        // Nền Gradient 3 màu: Hồng -> Tím Nhạt -> Trắng
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(SoftPink, SoftPurple, Color.White)
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 🔹 CARD CÀI ĐẶT QUYỀN
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = WhiteCard.copy(alpha = 0.9f)
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Quyền & cài đặt",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PurpleMain
                            )
                            Text(
                                "Để đọc biến động số dư từ SMS: cấp quyền khi app hỏi. " +
                                        "Để đọc thông báo ngân hàng/MoMo: bật Trợ năng → " +
                                        "Truy cập thông báo cho app này.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )

                            // Nút bấm màu Tím
                            Button(
                                onClick = {
                                    context.startActivity(
                                        Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                            ) {
                                Text("Mở cài đặt đọc thông báo", color = Color.White)
                            }

                            OutlinedButton(
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", context.packageName, null)
                                        )
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PurpleMain),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                            ) {
                                Text("Chi tiết quyền ứng dụng", color = PurpleMain)
                            }
                        }
                    }
                }

                // 🔹 TRẠNG THÁI TRỐNG
                if (notifications.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Chưa có thông báo nào",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                } else {
                    items(notifications) { notification ->
                        // Chú ý: Component NotificationItem nên được bạn chỉnh màu bên trong file của nó
                        NotificationItem(notification = notification)
                    }
                }
            }
        }
    }
}