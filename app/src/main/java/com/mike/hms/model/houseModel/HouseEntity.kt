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
){
    constructor() : this("", "", "", "", "")
}
