package com.mike.hms.model.houseModel

import androidx.room.Entity

@Entity(tableName = "HouseTable")
class HouseEntity(
    val houseID: String,
    val houseName: String,
    val houseType: String,
    val houseLocation: String,
    val houseCategory: String,
){
    constructor() : this("", "", "", "", "")
}

enum class HouseType{
    ECONOMY,
    LUXURY
}

enum class HouseCategory{
    MALE,
    FEMALE,
    MIXED
}