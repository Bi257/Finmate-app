package com.example.baitapdidongcuoiki.ui.market

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.baitapdidongcuoiki.R
import com.example.baitapdidongcuoiki.databinding.ActivityMarketBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MarketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarketBinding
    private lateinit var viewModel: MarketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MarketViewModel::class.java)

        setupSpinner()
        setupObservers()
        setupConvertButton()

        viewModel.refresh()
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.currency_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrency.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.rates.observe(this) { rates ->
            rates?.forEach { rate ->
                when (rate.currency) {
                    "USD" -> {
                        binding.tvUsdBuy.text = formatNumber(rate.buy)
                        binding.tvUsdSell.text = formatNumber(rate.sell)
                        binding.tvUsdChange.text = "-0.2%"
                    }
                    "EUR" -> {
                        binding.tvEurBuy.text = formatNumber(rate.buy)
                        binding.tvEurSell.text = formatNumber(rate.sell)
                        binding.tvEurChange.text = "+0.4%"
                    }
                    "JPY" -> {
                        binding.tvJpyBuy.text = formatNumber(rate.buy)
                        binding.tvJpySell.text = formatNumber(rate.sell)
                        binding.tvJpyChange.text = "+0.1%"
                    }
                }
            }
            binding.tvUpdatedTime.text = "Cập nhật ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())}"
        }

        viewModel.goldPrices.observe(this) { gold ->
            gold?.let {
                binding.tvSjcBuy.text = formatGoldPrice(it.sjcBuy)
                binding.tvSjcSell.text = formatGoldPrice(it.sjcSell)
                binding.tvSjcChange.text = formatChange(it.changePercentSjc)

                binding.tvGold9999Buy.text = formatGoldPrice(it.gold9999Buy)
                binding.tvGold9999Sell.text = formatGoldPrice(it.gold9999Sell)
                binding.tvGold9999Change.text = formatChange(it.changePercent9999)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Có thể hiển thị ProgressBar nếu muốn
        }

        viewModel.error.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    private fun setupConvertButton() {
        binding.btnConvert.setOnClickListener {
            val amountText = binding.etAmount.text.toString()
            if (amountText.isEmpty()) {
                Toast.makeText(this, "Nhập số lượng", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val amount = amountText.toDoubleOrNull() ?: 0.0
            val selected = binding.spinnerCurrency.selectedItem.toString()
            val rates = viewModel.rates.value
            val gold = viewModel.goldPrices.value

            val resultVnd = when (selected) {
                "USD" -> amount * (rates?.find { it.currency == "USD" }?.buy ?: 25400.0)
                "EUR" -> amount * (rates?.find { it.currency == "EUR" }?.buy ?: 27100.0)
                "JPY" -> amount * (rates?.find { it.currency == "JPY" }?.buy ?: 166.0)
                "Vàng SJC (lượng)" -> amount * (gold?.sjcBuy?.times(1_000_000) ?: 83.5e6)
                "Vàng 9999 (lượng)" -> amount * (gold?.gold9999Buy?.times(1_000_000) ?: 79.2e6)
                else -> 0.0
            }
            binding.tvConvertResult.text = "${formatNumber(amount)} $selected = ${formatNumber(resultVnd)} VNĐ"
        }
    }

    private fun formatNumber(value: Double): String {
        return NumberFormat.getNumberInstance(Locale.US).format(value)
    }

    private fun formatGoldPrice(value: Double): String {
        return "${String.format("%.1f", value)}tr"
    }

    private fun formatChange(percent: Double): String {
        val sign = if (percent >= 0) "+" else ""
        return "$sign${String.format("%.1f", percent)}%"
    }
}


































































