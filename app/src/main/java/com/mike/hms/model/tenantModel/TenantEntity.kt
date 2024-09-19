package com.mike.hms.model.tenantModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tenantTable")
data class TenantEntity(
    @PrimaryKey val tenantID: String,
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