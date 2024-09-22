package com.mike.hms.model.roomDatabase

import androidx.room.TypeConverter
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
}