package com.mike.hms.model.userModel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userID: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val phoneNumber: String,
    val role: String,
    val email: String
){
    constructor() : this("", "", "", "", "", "","")
}

@Entity(tableName = "credit_cards")
data class CreditCard(
    @PrimaryKey(autoGenerate = true) val cardId: Int = 0,
    val userId: String,
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String
)