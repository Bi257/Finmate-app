package com.example.baitapdidongcuoiki.domain.model

data class Transaction(
    val id: Int? = null,
    val title: String,
    val amount: Double,
    val type: String, // "income" | "expense"
    val date: Long,
    val note: String = ""
)