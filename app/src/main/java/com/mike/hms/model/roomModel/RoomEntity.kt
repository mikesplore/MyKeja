package com.mike.hms.model.roomModel

import androidx.room.Entity

@Entity(tableName = "RoomTable")
class RoomEntity(
    val roomID: String,
    val roomNumber: String,
    val roomType: String,
    val roomCapacity: String,
){
    constructor() : this("", "", "", "")

}


enum class RoomType{
    PRIVATE,
    SHARED
}


