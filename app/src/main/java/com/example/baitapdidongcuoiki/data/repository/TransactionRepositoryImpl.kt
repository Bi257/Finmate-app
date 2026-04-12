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

    // 1. Thêm giao dịch: Lưu local xong đẩy lên Cloud
    override suspend fun addTransaction(transaction: Transaction) {
        // Lưu Local (SQLite/Room)
        dao.insert(transaction.toEntity())

        // Đẩy lên Firestore
        val userId = auth.currentUser?.uid ?: return
        try {
            val transactionMap = hashMapOf(
                "title" to transaction.title,
                "amount" to transaction.amount,
                "type" to transaction.type,
                "date" to transaction.date,
                "note" to transaction.note,
                "timestamp" to System.currentTimeMillis() // Thêm thời gian để sắp xếp
            )
            firestore.collection("users").document(userId)
                .collection("transactions")
                .add(transactionMap)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 2. Lấy dữ liệu từ Room (UI luôn lấy từ đây để nhanh)
    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // 3. HÀM MỚI: Đồng bộ dữ liệu từ Cloud về Local (Call khi vào màn Home hoặc Refresh)
    suspend fun syncTransactionsFromCloud() {
        val userId = auth.currentUser?.uid ?: return
        try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("transactions")
                .get()
                .await()

            val cloudTransactions = snapshot.documents.mapNotNull { doc ->
                // Ánh xạ từ Document Firestore sang Domain Model của bạn
                Transaction(
                    title = doc.getString("title") ?: "",
                    amount = doc.getDouble("amount") ?: 0.0,
                    type = doc.getString("type") ?: "",
                    date = doc.getLong("date") ?: 0L,
                    note = doc.getString("note") ?: ""
                )
            }

            // Lưu tất cả dữ liệu mới tải về vào Room
            cloudTransactions.forEach { transaction ->
                dao.insert(transaction.toEntity())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}