package com.mike.hms.model.paymentMethods

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.mike.hms.model.userModel.UserEntity

@Entity(tableName = "credit_cards")
data class CreditCardEntity(
    @PrimaryKey val cardId: String,
    val userId: String = "",
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val balance: String = ""
){
    constructor() : this("", "", "", "", "", "")
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