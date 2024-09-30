package com.mike.hms.model.roomDatabase

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mike.hms.model.houseModel.HouseAmenities

class Converters {
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        return list?.joinToString(",")
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