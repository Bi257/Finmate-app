package com.example.baitapdidongcuoiki.domain.usecase

import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {

    operator fun invoke(): Flow<List<Transaction>> {

        return repository.getTransactions()

    }
}