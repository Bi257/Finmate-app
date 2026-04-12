package com.example.baitapdidongcuoiki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val base: String,
    val ratesJson: String,
    val timestamp: Long
)