package com.example.baitapdidongcuoiki.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ExchangeRateHostResponse(
    @SerializedName("base")

    val base: String,

    @SerializedName("date")

    val date: String,

    @SerializedName("rates")

    val rates: Map<String, Double>

)