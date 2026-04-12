package com.example.baitapdidongcuoiki.domain.usecase

data class UseCases(

    // 👉 Transaction (DB thật)
    val getTransactionsUseCase: GetTransactionsUseCase,
    val addTransactionUseCase: AddTransactionUseCase,

    // 👉 Tax (giữ logic cũ)
    val calculateTaxUseCase: CalculateTaxUseCase
) {

    // 👉 Wrapper cho tax (giữ tương thích code cũ nếu đang dùng)
    fun calculateTaxResult(
        grossIncome: Double,
        dependents: Int
    ): TaxResult {
        return calculateTaxUseCase(grossIncome, dependents)
    }
}