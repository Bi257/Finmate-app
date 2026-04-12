package com.example.baitapdidongcuoiki.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey val base: String,
    val ratesJson: String,
    val timestamp: Long
)