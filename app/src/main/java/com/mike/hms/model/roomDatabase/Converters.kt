package com.mike.hms.model.roomDatabase

import androidx.compose.ui.input.key.type
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mike.hms.model.houseModel.HouseAmenities
import com.mike.hms.model.roomModel.AmenityType

class Converters {
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    // Convert Set<AmenityType> to a comma-separated string for storage
    @TypeConverter
    fun fromSet(amenities: Set<AmenityType>): String {
        return amenities.joinToString(separator = ",") { it.name }
    }

    // Convert a comma-separated string back into a Set<AmenityType>
    @TypeConverter
    fun toSet(amenitiesString: String): Set<AmenityType> {
        return amenitiesString.split(",").map { AmenityType.valueOf(it) }.toSet()
    }

    @TypeConverter
    fun fromEnumList(houseAmenities: List<HouseAmenities>): String {
        return Gson().toJson(houseAmenities)
    }

    @TypeConverter
    fun toEnumList(houseAmenitiesString: String): List<HouseAmenities> {
        val type = object : TypeToken<List<HouseAmenities>>() {}.type
        return Gson().fromJson(houseAmenitiesString, type)
    }
}