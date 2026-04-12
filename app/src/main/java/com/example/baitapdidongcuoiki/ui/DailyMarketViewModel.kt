package com.example.baitapdidongcuoiki.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baitapdidongcuoiki.api.BinanceApi
import com.example.baitapdidongcuoiki.data.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import com.example.baitapdidongcuoiki.data.remote.dto.ExchangeRateDto

data class DailyMarketUiState(
    val rates: List<ExchangeRateDto> = emptyList(),
    val goldPricePerLuongVnd: Double = 0.0,      // giá vàng (VND/lượng) từ PAXG quy đổi
    val goldPriceMessage: String = "",            // thông báo về nguồn vàng
    val showDialog: Boolean = false,
    val dialogTitle: String = "Cập nhật từ nguồn dữ liệu",
    val message: String = "",
    val isLoading: Boolean = false,
    val isFailure: Boolean = false
)

@HiltViewModel
class DailyMarketViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
    private val binanceApi: BinanceApi
) : ViewModel() {

    private val _ui = MutableStateFlow(DailyMarketUiState())
    val ui: StateFlow<DailyMarketUiState> = _ui.asStateFlow()

    private val nf = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    private var initialLoadDone = false

    init {
        // ✅ Sửa lỗi: gọi suspend function bên trong viewModelScope
        viewModelScope.launch {
            loadMarketData()
        }
    }

    fun loadAndShowOnce() {
        if (!initialLoadDone) {
            initialLoadDone = true
            refreshMarketData()
        }
    }

    fun refreshMarketData() {
        viewModelScope.launch {
            _ui.update {
                it.copy(
                    isLoading = true,
                    showDialog = true,
                    dialogTitle = "Đang tải…",
                    message = "",
                    isFailure = false
                )
            }
            try {
                loadMarketData(showResultInDialog = true)
            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        dialogTitle = "Đã xảy ra sự cố",
                        message = "Không tải được dữ liệu thị trường.\n\n${e.message ?: "Lỗi không xác định"}\n\nKiểm tra mạng và bấm «Thử lại».",
                        isLoading = false,
                        showDialog = true,
                        isFailure = true
                    )
                }
            }
        }
    }

    private suspend fun loadMarketData(showResultInDialog: Boolean = false) {
        val rates = try {
            exchangeRepository.getExchangeRates()
        } catch (e: Exception) {
            emptyList()
        }
        val usdVnd = rates.find { it.currency == "USD" }?.buy ?: 0.0

        var goldPrice = 0.0
        var goldMessage = ""
        if (usdVnd > 0) {
            try {
                val paxgUsd = binanceApi.getTickerPrice("PAXGUSDT").price?.toDoubleOrNull()
                if (paxgUsd != null) {
                    val ozToGram = 31.1034768
                    val luongGram = 37.5
                    goldPrice = paxgUsd * usdVnd * (luongGram / ozToGram)
                    goldMessage = "Tham khảo quốc tế (PAXG): ${nf.format(goldPrice)} VND/lượng (37.5g)"
                } else {
                    goldMessage = "Không đọc được giá PAXG từ máy chủ."
                }
            } catch (e: Exception) {
                goldMessage = "Không lấy được giá vàng tham chiếu: ${e.message}"
            }
        } else {
            goldMessage = "Chưa có tỷ giá USD/VND để quy đổi vàng."
        }

        _ui.update {
            it.copy(
                rates = rates,
                goldPricePerLuongVnd = goldPrice,
                goldPriceMessage = goldMessage,
                isLoading = false,
                isFailure = rates.isEmpty() && goldPrice == 0.0
            )
        }

        if (showResultInDialog) {
            val title = if (rates.isNotEmpty() && goldPrice > 0) "Cập nhật từ nguồn dữ liệu"
            else if (rates.isEmpty()) "Đã xảy ra sự cố"
            else "Một phần dữ liệu không tải được"

            val message = buildString {
                if (rates.isNotEmpty()) {
                    append("Hôm nay 1 USD bằng ${nf.format(usdVnd)} đồng.\n(Nguồn: Frankfurter — tham khảo.)\n\n")
                } else {
                    append("Không lấy được tỷ giá USD/VND. Kiểm tra Wi‑Fi hoặc dữ liệu di động.\n\n")
                }
                if (goldPrice > 0) {
                    append("Giá vàng SJC niêm yết trong nước: app không có API chính thức từ SJC — vui lòng xem sjc.com.vn hoặc ngân hàng.\n\n")
                    append("Tham khảo quốc tế (PAXG): khoảng ${nf.format(goldPrice)} VND/lượng (37,5 g; không phải bằng SJC).")
                } else {
                    append(goldMessage)
                }
            }

            _ui.update {
                it.copy(
                    dialogTitle = title,
                    message = message,
                    showDialog = true,
                    isLoading = false
                )
            }
        }
    }

    fun dismissDialog() {
        _ui.update { it.copy(showDialog = false) }
    }
}