package com.example.baitapdidongcuoiki.data.mapper

import com.example.baitapdidongcuoiki.data.local.entity.TransactionEntity
import com.example.baitapdidongcuoiki.domain.model.Transaction


fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id ?: 0,
        title = title,
        amount = amount,
        type = type,
        date = date,
        note = note
    )
}
fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        title = title,
        amount = amount,
        type = type,
        date = date,
        note = note
    )
}