package com.example.baitapdidongcuoiki.ui.screen.notification

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SmsMessage(
    val id: Long,
    val address: String?,
    val body: String,
    val date: Long
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _smsMessages = MutableStateFlow<List<SmsMessage>>(emptyList())
    val smsMessages: StateFlow<List<SmsMessage>> = _smsMessages.asStateFlow()

    init {
        loadSmsDirectly()
    }

    private fun loadSmsDirectly() {
        viewModelScope.launch {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("NotificationVM", "Không có quyền READ_SMS")
                return@launch
            }
            val messages = withContext(Dispatchers.IO) {
                readAllSmsFromProvider(context.contentResolver)
            }
            _smsMessages.value = messages
            Log.d("NotificationVM", "Loaded ${messages.size} messages directly")
        }
    }

    private fun readAllSmsFromProvider(contentResolver: ContentResolver): List<SmsMessage> {
        val messages = mutableListOf<SmsMessage>()
        val uri = Uri.parse("content://sms/inbox")
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, "date DESC")
        cursor?.use {
            val idCol = it.getColumnIndex("_id")
            val addressCol = it.getColumnIndex("address")
            val bodyCol = it.getColumnIndex("body")
            val dateCol = it.getColumnIndex("date")
            while (it.moveToNext()) {
                val id = it.getLong(idCol)
                val address = if (addressCol >= 0) it.getString(addressCol) else null
                val body = if (bodyCol >= 0) it.getString(bodyCol) ?: "" else ""
                val date = if (dateCol >= 0) it.getLong(dateCol) else System.currentTimeMillis()
                messages.add(SmsMessage(id, address, body, date))
            }
        }
        return messages
    }

    fun refresh() {
        loadSmsDirectly()
    }
}