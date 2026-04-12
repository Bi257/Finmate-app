package com.example.baitapdidongcuoiki.data.repository

import com.example.baitapdidongcuoiki.data.local.TransactionDao
import com.example.baitapdidongcuoiki.data.mapper.toDomain
import com.example.baitapdidongcuoiki.data.mapper.toEntity
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.repository.TransactionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : TransactionRepository {

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("Chưa đăng nhập")
    }

    override suspend fun addTransaction(transaction: Transaction) {
        val userId = getCurrentUserId()
        // 1. Lưu vào Room với userId
        dao.insert(transaction.toEntity(userId = userId))

        // 2. Đẩy lên Firestore
        try {
            val transactionMap = hashMapOf(
                "title" to transaction.title,
                "amount" to transaction.amount,
                "type" to transaction.type,
                "date" to transaction.date,
                "note" to transaction.note
            )
            firestore.collection("users")
                .document(userId)
                .collection("transactions")
                .add(transactionMap)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getTransactions(): Flow<List<Transaction>> {
        val userId = getCurrentUserId()
        return dao.getTransactionsByUserId(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}