package com.mike.hms.model.userModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userTable")
data class UserEntity(
    @PrimaryKey val userID: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val phoneNumber: String,
    val role: String,
){
    constructor() : this("", "", "", "", "", "")
}

enum class Gender{
    MALE,
    FEMALE
}

enum class Role{
    TENANT,
    OWNER
}