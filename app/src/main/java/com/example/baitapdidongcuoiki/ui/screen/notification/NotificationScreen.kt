package com.example.baitapdidongcuoiki.ui.screen.notification

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val smsList by viewModel.smsMessages.collectAsStateWithLifecycle(initialValue = emptyList())
    val hasSmsPermission = remember {
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopAppBar(
                title = { Text("Thông báo", color = Color(0xFF4A148C)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                navigationIcon = {
                    IconButton(onClick = { /* đóng màn hình nếu cần */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF9C27B0))
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card hướng dẫn quyền
            item {
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
                        Text(
                            text = "Quyền & cài đặt",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF4A148C)
                        )
                        Text(
                            text = "Để đọc biến động số dư từ SMS, cấp quyền khi app hỏi. Để đọc thông báo ngân hàng/MoMo: bật Trợ năng → Truy cập thông báo cho app này.",
                            fontSize = 14.sp,
                            color = Color(0xFF6A1B9A)
                        )
                        if (!hasSmsPermission) {
                            Text(
                                text = "⚠️ Chưa cấp quyền đọc SMS. Vui lòng cấp quyền trong cài đặt.",
                                color = Color(0xFFF44336),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Danh sách tin nhắn SMS
            if (smsList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // ✅ SỬA LỖI: thay Icons.Default.Sms bằng Icons.Default.Message
                            Icon(
                                Icons.Default.Message,
                                contentDescription = null,
                                tint = Color(0xFFCE93D8),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Chưa có tin nhắn SMS hoặc biến động số dư nào",
                                color = Color(0xFF8E24AA),
                                fontSize = 16.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(smsList) { sms ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Tin nhắn từ ${sms.address ?: "Ngân hàng"}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A148C),
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = sms.body,
                                fontSize = 13.sp,
                                color = Color(0xFF6A1B9A),
                                maxLines = 3
                            )
                            Text(
                                text = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(Date(sms.date)),
                                fontSize = 11.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}