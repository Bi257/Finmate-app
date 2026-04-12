package com.example.baitapdidongcuoiki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,        // 👈 Thêm userId để phân biệt tài khoản
    val note: String,
    val amount: Double,
    val type: String,
    val title: String,
    val date: Long
)