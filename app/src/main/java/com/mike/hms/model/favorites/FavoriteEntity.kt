package com.mike.hms.model.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavouriteEntity")
data class FavouriteEntity(
    @PrimaryKey val favouriteID: String,
    val houseID: String,
)
{
    constructor() : this("", "")

}