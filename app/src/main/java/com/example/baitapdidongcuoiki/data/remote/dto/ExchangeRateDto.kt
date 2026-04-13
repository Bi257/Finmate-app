package com.example.baitapdidongcuoiki.data.remote.dto

data class ExchangeRateDto(
    val currency: String,

    val buy: Double,

    val sell: Double,

    val changePercent: Double = 0.0
)