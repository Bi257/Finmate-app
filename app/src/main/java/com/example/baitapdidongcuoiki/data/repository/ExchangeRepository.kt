package com.example.baitapdidongcuoiki.data.repository

import com.example.baitapdidongcuoiki.api.ExchangeApi
import com.example.baitapdidongcuoiki.api.ExchangeResponse
import com.example.baitapdidongcuoiki.data.local.ExchangeRateDao
import com.example.baitapdidongcuoiki.data.local.ExchangeRateEntity
import com.example.baitapdidongcuoiki.data.remote.dto.ExchangeRateDto
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRepository @Inject constructor(
    private val api: ExchangeApi,
    private val dao: ExchangeRateDao
) {
    private val gson = Gson()

    suspend fun getExchangeRates(): List<ExchangeRateDto> {
        return try {
            val response = api.getRates("USD")
            val ratesMap = response.rates
            val vndRate = ratesMap["VND"]
                ?: throw IllegalStateException("API không trả về VND")

            val targetCurrencies = listOf("USD", "EUR", "JPY")
            val dtoList = targetCurrencies.mapNotNull { currency ->
                val rateInVnd = when (currency) {
                    "USD" -> vndRate
                    "EUR" -> ratesMap["EUR"]?.let { it * vndRate }
                    "JPY" -> ratesMap["JPY"]?.let { it * vndRate }
                    else -> null
                }
                rateInVnd?.let {
                    ExchangeRateDto(
                        currency = currency,
                        buy = it,
                        sell = it * 1.01,
                        changePercent = 0.0
                    )
                }
            }

            // Lưu cache
            dao.insert(
                ExchangeRateEntity(
                    base = "USD",
                    ratesJson = gson.toJson(response),
                    timestamp = System.currentTimeMillis()
                )
            )
            dtoList
        } catch (e: Exception) {
            e.printStackTrace()
            loadCachedRates()
        }
    }

    private suspend fun loadCachedRates(): List<ExchangeRateDto> {
        return try {
            val cached = dao.getRates("USD")
            if (cached != null) {
                val response = gson.fromJson(cached.ratesJson, ExchangeResponse::class.java)
                val ratesMap = response.rates
                val vndRate = ratesMap["VND"] ?: throw IllegalStateException("Cache lỗi: không có VND")
                val targetCurrencies = listOf("USD", "EUR", "JPY")
                targetCurrencies.mapNotNull { currency ->
                    val rateInVnd = when (currency) {
                        "USD" -> vndRate
                        "EUR" -> ratesMap["EUR"]?.let { it * vndRate }
                        "JPY" -> ratesMap["JPY"]?.let { it * vndRate }
                        else -> null
                    }
                    rateInVnd?.let {
                        ExchangeRateDto(currency, it, it * 1.01, 0.0)
                    }
                }
            } else {
                defaultRates()
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            defaultRates()
        } catch (e: Exception) {
            e.printStackTrace()
            defaultRates()
        }
    }

    private fun defaultRates(): List<ExchangeRateDto> {
        return listOf(
            ExchangeRateDto("USD", 25400.0, 25620.0, 0.0),
            ExchangeRateDto("EUR", 27100.0, 27490.0, 0.0),
            ExchangeRateDto("JPY", 166.0, 173.0, 0.0)
        )
    }

    suspend fun getUsdToVndRate(): Double {
        val rates = getExchangeRates()
        return rates.find { it.currency == "USD" }?.buy ?: 25400.0
    }
}