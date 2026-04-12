package com.example.baitapdidongcuoiki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.baitapdidongcuoiki.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, ExchangeRateEntity::class],
    version = 3, // 🔥 tăng version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun exchangeRateDao(): ExchangeRateDao

}