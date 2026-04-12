package com.example.baitapdidongcuoiki.data.local

import androidx.room.*
import com.example.baitapdidongcuoiki.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    fun getTransactionsByUserId(userId: String): Flow<List<TransactionEntity>>

    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE userId = :userId")
    suspend fun getAllOnceByUserId(userId: String): List<TransactionEntity>
}