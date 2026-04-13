package com.example.baitapdidongcuoiki.data.local

import androidx.room.*

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM exchange_rates WHERE base = :base LIMIT 1")
    suspend fun getRates(base: String): ExchangeRateEntity?



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rate: ExchangeRateEntity)
}