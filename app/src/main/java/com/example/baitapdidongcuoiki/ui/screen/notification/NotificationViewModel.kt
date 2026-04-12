package com.example.baitapdidongcuoiki.ui.screen.notification

import androidx.lifecycle.ViewModel
import com.example.baitapdidongcuoiki.service.SmsRepository
import androidx.lifecycle.viewModelScope
import com.example.baitapdidongcuoiki.domain.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val smsRepository: SmsRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications
    init {
        // Lắng nghe dữ liệu SMS & Notification thật
        smsRepository.getAllNotifications()
            .onEach { _notifications.value = it }
            .launchIn(viewModelScope)
    }

    // Refresh thủ công (dùng cho Pull-to-Refresh nếu bạn thêm sau)
    fun refresh() {
        // Repository đã dùng Flow nên không cần gọi lại
    }
}