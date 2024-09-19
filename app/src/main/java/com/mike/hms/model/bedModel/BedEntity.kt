package com.mike.hms.model.bedModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BedTable")
class BedEntity(
    @PrimaryKey val bedID: String,
    val bedType: String,
    val bedStatus: String,
    val roomID: String,
)