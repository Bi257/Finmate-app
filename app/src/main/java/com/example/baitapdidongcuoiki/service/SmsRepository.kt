package com.example.baitapdidongcuoiki.service

import android.content.Context
import android.util.Log
import com.example.baitapdidongcuoiki.domain.model.Notification
import com.example.baitapdidongcuoiki.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsRepository @Inject constructor() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    private val notificationsFlow = _notifications.asStateFlow()

    fun insertNotification(notification: Notification) {
        _notifications.value = _notifications.value + notification
        Log.d("SmsRepository", "📩 Thêm thông báo: ${notification.title}")
    }

    fun getAllNotifications(): Flow<List<Notification>> = notificationsFlow

    fun insert(context: Context, transaction: Transaction) {
        Log.d("DB", "Insert Transaction: $transaction")

        // TODO: sau này lưu Room thật
    }
}