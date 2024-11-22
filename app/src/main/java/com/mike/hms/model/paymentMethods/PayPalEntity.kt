package com.mike.hms.model.paymentMethods

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.mike.hms.model.userModel.UserEntity

@Entity(tableName = "paypal")
data class PayPalEntity(
    @PrimaryKey val paypalId: String,
    val userId: String,
    val paypalEmail: String
) {
    constructor() : this("", "", "")

}

data class PayPalWithUser(
    @Embedded val paypal: PayPalEntity,
    @Relation(
        parentColumn = "userId", // Column in PayPalEntity
        entityColumn = "userID" // Column in UserEntity
    )
    val user: UserEntity
) {
    constructor() : this(PayPalEntity(), UserEntity())
}