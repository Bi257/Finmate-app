package com.example.baitapdidongcuoiki.api

import com.example.baitapdidongcuoiki.data.remote.dto.ExchangeRateHostResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeApi {
    @GET("latest")
    suspend fun getRates(@Query("base") base: String = "USD"): ExchangeResponse
}