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

