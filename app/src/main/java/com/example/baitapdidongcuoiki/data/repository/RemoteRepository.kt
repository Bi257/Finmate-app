package com.example.baitapdidongcuoiki.data.repository

import android.util.Log
import com.example.baitapdidongcuoiki.api.BinanceApi
import com.example.baitapdidongcuoiki.data.local.ExchangeRateDao
import com.example.baitapdidongcuoiki.data.local.ExchangeRateEntity
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Đồng bộ dữ liệu “backend” theo đề: tỷ giá ngoại tệ (USD, VND) + tham chiếu giá vàng (PAXG).
 * Đồng bộ giao dịch Firestore theo user vẫn do [TransactionRepositoryImpl] khi thêm/ghi.
 */
@Singleton
class RemoteRepository @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
    private val binanceApi: BinanceApi,
    private val dao: ExchangeRateDao          // thêm để lưu giá vàng
) {
    private val gson = Gson()

    suspend fun syncWithBackend() {
        // 1. Lấy tỷ giá (USD, EUR, JPY → VND) - không cần truyền base, hàm tự xử lý
        runCatching { exchangeRepository.getExchangeRates() }
            .onFailure { Log.w(TAG, "Exchange rates: ${it.message}") }

        // 2. Lấy giá vàng tham chiếu từ Binance (PAXG/USDT)
        runCatching {
            val price = binanceApi.getTickerPrice("PAXGUSDT").price?.toDoubleOrNull()
            if (price != null) {
                cacheGoldPrice(price)
                Log.d(TAG, "Gold spot (PAXG/USDT): $price")
            }
        }.onFailure { Log.w(TAG, "Gold spot: ${it.message}") }
    }

    /** Lưu giá vàng (USD/ounce) vào database dưới dạng cache */
    private suspend fun cacheGoldPrice(usdPerToken: Double) {
        dao.insert(
            ExchangeRateEntity(
                base = "GOLD_PAXG_USD",
                ratesJson = gson.toJson(mapOf("USD" to usdPerToken)),
                timestamp = System.currentTimeMillis()
            )
        )
    }

    private companion object {
        private const val TAG = "RemoteRepository"
    }
}