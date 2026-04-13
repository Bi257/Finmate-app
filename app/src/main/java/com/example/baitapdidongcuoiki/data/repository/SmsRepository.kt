package com.example.baitapdidongcuoiki.data.repository

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import com.example.baitapdidongcuoiki.data.local.SmsMessageDao
import com.example.baitapdidongcuoiki.data.local.entity.SmsMessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsRepository @Inject constructor(
    private val dao: SmsMessageDao
) {
    fun getAllSms(): Flow<List<SmsMessageEntity>> = dao.getAll()

    suspend fun insert(entity: SmsMessageEntity) {
        dao.insert(entity)
    }

    suspend fun getCount(): Int = dao.getCount()

    suspend fun syncSmsFromDevice(contentResolver: ContentResolver) {
        withContext(Dispatchers.IO) {
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
                    val entity = SmsMessageEntity(id, address, body, date)
                    dao.insert(entity)
                }
            }
        }
    }
}