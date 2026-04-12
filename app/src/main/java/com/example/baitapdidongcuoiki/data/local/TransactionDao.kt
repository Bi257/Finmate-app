package com.example.baitapdidongcuoiki.data.local

import androidx.room.*
import com.example.baitapdidongcuoiki.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions")
    suspend fun getAllOnce(): List<TransactionEntity>
}