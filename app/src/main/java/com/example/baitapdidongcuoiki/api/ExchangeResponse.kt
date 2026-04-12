package com.example.baitapdidongcuoiki.api

data class ExchangeResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)