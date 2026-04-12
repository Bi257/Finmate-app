package com.example.baitapdidongcuoiki.data.remote

import com.example.baitapdidongcuoiki.data.local.entity.TransactionEntity
import com.example.baitapdidongcuoiki.data.remote.dto.ExchangeRateDto
import retrofit2.Response
import retrofit2.http.*
import com.example.baitapdidongcuoiki.data.remote.dto.BalanceRequest

interface ApiService {

    // ===== 🔥 LẤY TỶ GIÁ =====
    @GET("api/exchange-rates")
    suspend fun getExchangeRates(): Response<List<ExchangeRateDto>>

    // ===== 🔥 SYNC TRANSACTION =====
    @POST("api/transactions/sync")
    suspend fun syncTransactions(
        @Body transactions: List<TransactionEntity>
    ): Response<Unit>

    // ===== 🔥 UPDATE BALANCE =====
    @PUT("api/wallet/balance")
    suspend fun updateWalletBalance(
        @Body request: BalanceRequest
    ): Response<Unit>
}

/**
 * 👉 Request model (chuẩn backend)
 */
data class BalanceRequest(
    val balance: Double
)