package com.example.baitapdidongcuoiki.domain.usecase
import javax.inject.Inject
import kotlin.math.max

data class TaxResult(
    val taxAmount: Double = 0.0,
    val taxableIncome: Double = 0.0,
    val dependantDeduction: Double = 0.0,
    val personalDeduction: Double = 11_000_000.0
)

class CalculateTaxUseCase @Inject constructor() {

    operator fun invoke(grossIncome: Double, dependents: Int): TaxResult {

        val personalDeduction = 11_000_000.0
        val dependantDeduction = dependents * 4_400_000.0
        val totalDeduction = personalDeduction + dependantDeduction

        val taxableIncome = max(0.0, grossIncome - totalDeduction)

        val taxAmount = calculateProgressiveTax(taxableIncome)

        return TaxResult(
            taxAmount = taxAmount,
            taxableIncome = taxableIncome,
            dependantDeduction = dependantDeduction,
            personalDeduction = personalDeduction
        )
    }

    private fun calculateProgressiveTax(income: Double): Double {
        return when {
            income <= 0 -> 0.0
            income <= 5_000_000 -> income * 0.05
            income <= 10_000_000 -> income * 0.1 - 250_000
            income <= 18_000_000 -> income * 0.15 - 750_000
            income <= 32_000_000 -> income * 0.2 - 1_650_000
            income <= 52_000_000 -> income * 0.25 - 3_250_000
            income <= 80_000_000 -> income * 0.3 - 5_850_000
            else -> income * 0.35 - 9_850_000
        }
    }
}