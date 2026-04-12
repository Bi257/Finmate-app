package com.example.baitapdidongcuoiki.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

data class BinancePriceResponse(
    @SerializedName("symbol") val symbol: String? = null,
    @SerializedName("price") val price: String? = null
)

/** Giá PAXG/USDT ~ tham chiếu giá vàng (1 token ≈ 1 oz vàng). */
interface BinanceApi {
    @GET("api/v3/ticker/price")
    suspend fun getTickerPrice(@Query("symbol") symbol: String): BinancePriceResponse
}
