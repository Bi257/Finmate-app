package com.example.baitapdidongcuoiki.data.local

import androidx.room.*
import com.example.baitapdidongcuoiki.data.local.entity.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {
    @Query("SELECT * FROM exchange_rates ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ExchangeRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExchangeRateEntity)

    @Query("SELECT * FROM exchange_rates WHERE base = :base ORDER BY timestamp DESC LIMIT 1")
    suspend fun getRates(base: String): ExchangeRateEntity?
}