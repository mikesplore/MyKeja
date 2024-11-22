package com.mike.hms.model.paymentMethods

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.mike.hms.model.userModel.UserEntity

@Entity(tableName = "mpesa")
data class MpesaEntity(
    @PrimaryKey val mpesaId: String,
    val userId: String,
    val phoneNumber: String,
){
    constructor() : this("", "", "")

}

data class MpesaWithUser(
    @Embedded val mpesa: MpesaEntity,
    @Relation(
        parentColumn = "userId", // Column in MpesaEntity
        entityColumn = "userID" // Column in UserEntity
    )
    val user: UserEntity
) {
    constructor() : this(MpesaEntity(), UserEntity())
}