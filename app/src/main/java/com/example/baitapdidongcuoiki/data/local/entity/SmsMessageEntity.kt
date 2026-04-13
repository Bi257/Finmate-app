package com.example.baitapdidongcuoiki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sms_messages")
data class SmsMessageEntity(
    @PrimaryKey
    val id: Long,
    val address: String?,
    val body: String,
    val date: Long,
    val createdAt: Long = System.currentTimeMillis()
)