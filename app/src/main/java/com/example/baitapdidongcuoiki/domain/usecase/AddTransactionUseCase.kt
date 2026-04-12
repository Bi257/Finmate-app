package com.example.baitapdidongcuoiki.domain.usecase

import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {

    suspend operator fun invoke(transaction: Transaction) {

        // 👉 Validate dữ liệu (giữ logic cũ + nâng cấp nhẹ)
        validate(transaction)

        // 👉 Insert vào DB (Room)
        repository.addTransaction(transaction)
    }

    // 👉 Tách riêng để dễ test + clean code
    private fun validate(transaction: Transaction) {

        if (transaction.title.isBlank()) {
            throw IllegalArgumentException("Title cannot be empty")
        }

        if (transaction.amount <= 0) {
            throw IllegalArgumentException("Amount must be greater than 0")
        }

        if (transaction.type.isBlank()) {
            throw IllegalArgumentException("Type is required")
        }

        // 👉 Chuẩn hóa type (tránh lỗi UI)
        val type = transaction.type.lowercase()
        if (type != "income" && type != "expense") {
            throw IllegalArgumentException("Type must be 'income' or 'expense'")
        }
    }
}