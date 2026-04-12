package com.example.baitapdidongcuoiki.ui.screen.tax

import androidx.lifecycle.ViewModel
import com.example.baitapdidongcuoiki.domain.usecase.CalculateTaxUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

data class TaxState(
    val salaryInput: String = "",
    val rentalInput: String = "",
    val otherIncomeInput: String = "",
    val dependents: Int = 0,
    val grossIncome: Double = 0.0,
    val tax: Double = 0.0,
    val taxableIncome: Double = 0.0,
    val dependantDeduction: Double = 0.0,
    val personalDeduction: Double = 11_000_000.0 * 12
)

@HiltViewModel
class TaxViewModel @Inject constructor(
    private val calculateTaxUseCase: CalculateTaxUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaxState())
    val state: StateFlow<TaxState> = _state.asStateFlow()

    private val money = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    init {
        recalculate()
    }

    fun onSalaryChange(raw: String) {
        _state.update { it.copy(salaryInput = raw.filter { c -> c.isDigit() }) }
        recalculate()
    }

    fun onRentalChange(raw: String) {
        _state.update { it.copy(rentalInput = raw.filter { c -> c.isDigit() }) }
        recalculate()
    }

    fun onOtherIncomeChange(raw: String) {
        _state.update { it.copy(otherIncomeInput = raw.filter { c -> c.isDigit() }) }
        recalculate()
    }

    fun incrementDependents() {
        _state.update { it.copy(dependents = (it.dependents + 1).coerceAtMost(20)) }
        recalculate()
    }

    fun decrementDependents() {
        _state.update { it.copy(dependents = (it.dependents - 1).coerceAtLeast(0)) }
        recalculate()
    }

    private fun recalculate() {
        val s = _state.value
        val gross = listOf(s.salaryInput, s.rentalInput, s.otherIncomeInput)
            .sumOf { it.toDoubleOrNull() ?: 0.0 }
        val result = calculateTaxUseCase(gross, s.dependents)
        _state.update {
            it.copy(
                grossIncome = gross,
                tax = result.taxAmount,
                taxableIncome = result.taxableIncome,
                dependantDeduction = result.dependantDeduction,
                personalDeduction = result.personalDeduction
            )
        }
    }

    fun settlementSummaryText(): String {
        val s = _state.value
        return buildString {
            appendLine("Quyết toán TNCN (lũy tiến)")
            appendLine("Tổng thu nhập: ${money.format(s.grossIncome)}")
            appendLine("Giảm trừ bản thân: ${money.format(s.personalDeduction)}")
            appendLine("Giảm trừ NPT (${s.dependents}): ${money.format(s.dependantDeduction)}")
            appendLine("Thu nhập chịu thuế: ${money.format(s.taxableIncome)}")
            appendLine("Thuế phải nộp: ${money.format(s.tax)}")
        }
    }
}