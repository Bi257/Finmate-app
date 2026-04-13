package com.example.baitapdidongcuoiki.domain.usecase

data class UseCases(

    // Transaction (DB thật)
    val getTransactionsUseCase: GetTransactionsUseCase,
    val addTransactionUseCase: AddTransactionUseCase,

    // Tax
    val calculateTaxUseCase: CalculateTaxUseCase
) {

    // Wrapper cho tax
    fun calculateTaxResult(
        grossIncome: Double,
        dependents: Int
    ): TaxResult {
        return calculateTaxUseCase(grossIncome, dependents)
    }
}