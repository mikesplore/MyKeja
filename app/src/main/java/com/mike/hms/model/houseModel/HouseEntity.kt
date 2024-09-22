package com.mike.hms.model.houseModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HouseTable")
class HouseEntity(
    @PrimaryKey val houseID: String,
    val houseName: String,
    val houseType: String,
    val houseImageLink: String,
    val houseLocation: String,
    val houseCategory: String,
){
    constructor() : this("", "", "", "","", "")
}

@Entity(tableName = "HouseTypes")
class HouseType(
    @PrimaryKey val houseTypeID: String,
    val houseType: String,
    val houseTypeImageLink: String,
){
    constructor() : this("", "", "")

}