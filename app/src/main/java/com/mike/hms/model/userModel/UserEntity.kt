package com.mike.hms.model.userModel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userID: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val phoneNumber: String,
    val role: String,
    val email: String,
    val photoUrl: String,
){
    constructor() : this("", "", "", "", "", "","","")
}

@Entity(tableName = "credit_cards")
data class CreditCardEntity(
    @PrimaryKey(autoGenerate = true) val cardId: Int = 0,
    val userId: String,
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String
){
    constructor() : this(0, "", "", "", "")
}

data class CreditCardWithUser(
    @Embedded val creditCard: CreditCardEntity,
    @Relation(
        parentColumn = "userId", // Column in CreditCard
        entityColumn = "userID" // Column in UserEntity
    )
    val user: UserEntity
){
    constructor() : this(CreditCardEntity(), UserEntity())

}