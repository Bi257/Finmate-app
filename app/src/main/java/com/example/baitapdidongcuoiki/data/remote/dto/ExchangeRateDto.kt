package com.example.baitapdidongcuoiki.data.remote.dto

data class ExchangeRateDto(
    val currency: String,   // "USD", "EUR", "JPY"
    val buy: Double,        // giá mua VND (dùng rate từ API)
    val sell: Double,       // giá bán = buy * 1.01 (hoặc + phí)
    val changePercent: Double = 0.0  // có thể tính sau
)