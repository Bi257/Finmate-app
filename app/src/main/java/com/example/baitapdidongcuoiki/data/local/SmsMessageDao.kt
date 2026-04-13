package com.example.baitapdidongcuoiki.data.local

import androidx.room.*
import com.example.baitapdidongcuoiki.data.local.entity.SmsMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SmsMessageDao {
    @Query("SELECT * FROM sms_messages ORDER BY date DESC")
    fun getAll(): Flow<List<SmsMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: SmsMessageEntity)

    @Query("SELECT COUNT(*) FROM sms_messages")
    suspend fun getCount(): Int

    @Query("SELECT * FROM sms_messages WHERE id = :id LIMIT 1")
    suspend fun getAllOnceById(id: Long): SmsMessageEntity?
}