package com.example.baitapdidongcuoiki.domain.usecase

import com.example.baitapdidongcuoiki.domain.repository.TransactionRepository

class RefreshTransactionsUseCase(
    private val repository: TransactionRepository
) {


    suspend operator fun invoke() = repository.refreshTransactions()



}