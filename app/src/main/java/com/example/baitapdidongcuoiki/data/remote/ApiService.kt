package com.example.baitapdidongcuoiki.data.remote

import com.example.baitapdidongcuoiki.data.local.entity.TransactionEntity
import com.example.baitapdidongcuoiki.data.remote.dto.ExchangeRateDto
import retrofit2.Response
import retrofit2.http.*
import com.example.baitapdidongcuoiki.data.remote.dto.BalanceRequest

interface ApiService {

    // lấy tỷ giá
    @GET("api/exchange-rates")
    suspend fun getExchangeRates(): Response<List<ExchangeRateDto>>

    @POST("api/transactions/sync")
    suspend fun syncTransactions(
        @Body transactions: List<TransactionEntity>
    ): Response<Unit>

    @PUT("api/wallet/balance")
    suspend fun updateWalletBalance(
        @Body request: BalanceRequest
    ): Response<Unit>
}

data class BalanceRequest(
    val balance: Double
)