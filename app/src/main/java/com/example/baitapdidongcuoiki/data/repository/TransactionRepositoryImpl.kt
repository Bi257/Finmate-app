package com.example.baitapdidongcuoiki.data.repository

import com.example.baitapdidongcuoiki.data.local.TransactionDao
import com.example.baitapdidongcuoiki.data.mapper.toDomain
import com.example.baitapdidongcuoiki.data.mapper.toEntity
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.repository.TransactionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : TransactionRepository {

    override suspend fun addTransaction(transaction: Transaction) {
        // 1. Lưu vào Room (local cache)
        dao.insert(transaction.toEntity())

        // 2. Đẩy lên Firestore
        try {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val transactionMap = hashMapOf(
                    "title" to transaction.title,
                    "amount" to transaction.amount,
                    "type" to transaction.type,
                    "date" to transaction.date,
                    "note" to transaction.note,
                    "createdAt" to System.currentTimeMillis()
                )
                firestore.collection("users")
                    .document(userId)
                    .collection("transactions")
                    .add(transactionMap)
                    .await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getTransactions(): Flow<List<Transaction>> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val subscription = firestore.collection("users")
            .document(userId)
            .collection("transactions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val transactions = snapshot.documents.mapNotNull { doc ->
                        Transaction(
                            title = doc.getString("title") ?: "",
                            amount = doc.getDouble("amount") ?: 0.0,
                            type = doc.getString("type") ?: "",
                            date = doc.getLong("date") ?: 0L,
                            note = doc.getString("note") ?: ""
                        )
                    }
                    trySend(transactions)
                }
            }
        awaitClose { subscription.remove() }
    }

    // 👇 THÊM HÀM NÀY ĐỂ REFRESH (BUỘC LOAD LẠI TỪ ROOM HOẶC FIRESTORE)
    override suspend fun refreshTransactions() {
        // Không cần làm gì vì callbackFlow tự động, nhưng để đảm bảo, có thể đọc từ Firestore một lần
        val userId = auth.currentUser?.uid ?: return
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("transactions")
            .get()
            .await()
        val transactions = snapshot.documents.mapNotNull { doc ->
            Transaction(
                title = doc.getString("title") ?: "",
                amount = doc.getDouble("amount") ?: 0.0,
                type = doc.getString("type") ?: "",
                date = doc.getLong("date") ?: 0L,
                note = doc.getString("note") ?: ""
            )
        }
        // Cập nhật cache Room (tùy chọn)
        // dao.clearAndInsertAll(transactions.map { it.toEntity() })
    }
}