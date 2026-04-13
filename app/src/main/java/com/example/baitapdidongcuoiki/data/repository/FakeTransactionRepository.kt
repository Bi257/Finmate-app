package com.example.baitapdidongcuoiki.data.repository

import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeTransactionRepository : TransactionRepository {

    private val _data = MutableStateFlow<List<Transaction>>(emptyList())

    override fun getTransactions(): Flow<List<Transaction>> {
        return _data.asStateFlow()
    }

    override suspend fun addTransaction(transaction: Transaction) {
        _data.update { currentList ->
            currentList + transaction
        }
    }

}