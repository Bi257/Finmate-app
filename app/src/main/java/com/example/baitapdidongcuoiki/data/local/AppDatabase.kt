package com.example.baitapdidongcuoiki.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.baitapdidongcuoiki.data.local.ExchangeRateEntity   // 👈 thêm dòng này
import com.example.baitapdidongcuoiki.data.local.entity.SmsMessageEntity
import com.example.baitapdidongcuoiki.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, ExchangeRateEntity::class, SmsMessageEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun smsMessageDao(): SmsMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tax_assistant.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}