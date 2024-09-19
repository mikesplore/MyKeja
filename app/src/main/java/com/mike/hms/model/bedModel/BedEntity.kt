package com.mike.hms.model.bedModel

import androidx.room.Entity

@Entity(tableName = "BedTable")
class BedEntity(
    val bedID: String,
    val bedType: String,
    val bedStatus: String,
    val roomID: String,
)