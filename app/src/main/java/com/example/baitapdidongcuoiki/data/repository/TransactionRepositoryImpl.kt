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
    private val firestore: FirebaseFirestore, // 🔹 Inject thêm Firestore
    private val auth: FirebaseAuth           // 🔹 Inject thêm Auth để lấy ID người dùng
) : TransactionRepository {

    override suspend fun addTransaction(transaction: Transaction) {
        // 1. Luôn luôn lưu vào Room (Local) trước để app chạy mượt khi offline
        dao.insert(transaction.toEntity())

        // 2. Đẩy lên Cloud Firestore
        try {
            val userId = auth.currentUser?.uid
            if (userId != null) {
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
                    .await() // Đợi đẩy lên thành công
            }
        } catch (e: Exception) {
            e.printStackTrace() // Log lỗi nếu không lưu được Cloud (ví dụ mất mạng)
        }
    }

    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}