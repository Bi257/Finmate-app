package com.example.baitapdidongcuoiki.data.repository

import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.repository.TransactionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeTransactionRepository : TransactionRepository {

    // Sử dụng MutableStateFlow để lưu trữ dữ liệu giả lập trong bộ nhớ
    private val _data = MutableStateFlow<List<Transaction>>(emptyList())

    // 1. Ghi đè hàm lấy danh sách giao dịch
    override fun getTransactions(): Flow<List<Transaction>> {
        return _data.asStateFlow()

    }

    // 2. Ghi đè hàm thêm giao dịch (Đảm bảo có từ khóa 'suspend' nếu Interface có)
    override suspend fun addTransaction(transaction: Transaction) {
        _data.update { currentList ->
            currentList + transaction

        }
    }
    override suspend fun syncTransactionsFromCloud() {
        // Vì đây là Repository giả để test hoặc preview UI,
        // nên chúng ta không cần làm gì ở đây cả.
        delay(1000) // Giả lập chờ 1 giây nếu muốn
    }

    // 3. Ghi đè hàm xóa giao dịch (Hilt/Interface yêu cầu phải có hàm này)

}