package com.example.baitapdidongcuoiki.domain.repository

import com.example.baitapdidongcuoiki.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun addTransaction(transaction: Transaction)



    fun getTransactions(): Flow<List<Transaction>>



    suspend fun refreshTransactions()


}