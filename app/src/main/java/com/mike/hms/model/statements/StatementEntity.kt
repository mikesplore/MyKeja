package com.mike.hms.model.statements

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statement")
data class StatementEntity(
    @PrimaryKey val statementID: String,
    val paymentMethod: PaymentMethod,
    val amount: String,
    val date: String,
    val houseID: String,
    val userId: String
){
    constructor(): this("", PaymentMethod.CREDIT_CARD,"","","","")
}

enum class PaymentMethod {
    CREDIT_CARD,
    MPESA,
    PAYPAL
}