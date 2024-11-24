package com.mike.hms.model.statements

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey val transactionID: String,
    val transactionNo: String,
    val paymentMethod: PaymentMethod,
    val amount: String,
    val date: String,
    val userId: String,
    val transactionType: TransactionType
){
    constructor(): this("","", PaymentMethod.CREDIT_CARD, "", "", "", TransactionType.ADDITION)
}

enum class PaymentMethod {
    CREDIT_CARD,
    MPESA,
    PAYPAL
}

enum class TransactionType {
    ADDITION,
    SUBTRACTION
}