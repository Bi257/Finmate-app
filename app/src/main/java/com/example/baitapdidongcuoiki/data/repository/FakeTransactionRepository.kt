package com.example.baitapdidongcuoiki.data.repository

import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTransactionRepository : TransactionRepository {
    override suspend fun addTransaction(transaction: Transaction) {
        // fake: không làm gì
    }

    override fun getTransactions(): Flow<List<Transaction>> {
        return flowOf(emptyList())
    }

    override suspend fun refreshTransactions() {
        // 👈 THÊM DÒNG NÀY
        // fake: không cần xử lý
    }
}