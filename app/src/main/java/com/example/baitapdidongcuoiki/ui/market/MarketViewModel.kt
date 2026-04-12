package com.example.baitapdidongcuoiki.ui.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baitapdidongcuoiki.data.remote.dto.ExchangeRateDto
import com.example.baitapdidongcuoiki.data.repository.ExchangeRepository
import com.example.baitapdidongcuoiki.api.BinanceApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
    private val binanceApi: BinanceApi
) : ViewModel() {

    private val _rates = MutableLiveData<List<ExchangeRateDto>>()
    val rates: LiveData<List<ExchangeRateDto>> = _rates

    private val _goldPrices = MutableLiveData<GoldPrices>()
    val goldPrices: LiveData<GoldPrices> = _goldPrices

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    data class GoldPrices(
        val sjcBuy: Double,   // triệu VND
        val sjcSell: Double,
        val gold9999Buy: Double,
        val gold9999Sell: Double,
        val changePercentSjc: Double,
        val changePercent9999: Double
    )

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Lấy tỷ giá
                val rates = exchangeRepository.getExchangeRates()
                _rates.value = rates

                // 2. Lấy giá vàng (mock + quy đổi từ PAXG nếu muốn)
                val gold = fetchGoldPrices()
                _goldPrices.value = gold
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Không thể tải dữ liệu: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchGoldPrices(): GoldPrices {
        // Giải pháp: dùng giá PAXG (USD/ounce) quy đổi ra VND/lượng
        // 1 lượng SJC = 37.5g, 1 ounce = 31.1035g => 1 lượng = 1.20556 ounce
        val ounceToLuong = 37.5 / 31.1035
        val usdToVnd = exchangeRepository.getUsdToVndRate()
        val paxgUsd = try {
            binanceApi.getTickerPrice("PAXGUSDT").price?.toDoubleOrNull() ?: 2000.0
        } catch (e: Exception) {
            2000.0
        }
        val goldPriceVndPerOunce = paxgUsd * usdToVnd
        val goldPriceVndPerLuong = goldPriceVndPerOunce * ounceToLuong  // VND/lượng
        val goldPriceTriệu = goldPriceVndPerLuong / 1_000_000

        // Tạo biến động giả (có thể random nhẹ)
        val sjcBuy = goldPriceTriệu
        val sjcSell = goldPriceTriệu * 1.02
        val gold9999Buy = goldPriceTriệu * 0.97
        val gold9999Sell = goldPriceTriệu * 0.98

        return GoldPrices(
            sjcBuy = sjcBuy,
            sjcSell = sjcSell,
            gold9999Buy = gold9999Buy,
            gold9999Sell = gold9999Sell,
            changePercentSjc = 1.2,
            changePercent9999 = 0.8
        )
    }

    fun refresh() = loadData()
}