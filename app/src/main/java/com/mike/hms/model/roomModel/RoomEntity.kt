package com.mike.hms.model.roomModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RoomTable")
class RoomEntity(
    @PrimaryKey val roomID: String,
    val roomNumber: String,
    val roomType: String,
    val roomCapacity: String,
    val roomAvailability: String,
){
    constructor() : this("", "", "", "", "")

}

@Entity(tableName = "RoomAllocation")
class RoomAllocationEntity(
    @PrimaryKey val roomAllocationID: String,
    val roomID: String,
    val bedID: String,
    val tenantID: String,
    val checkInDate: String,
    val checkOutDate: String,
){
    constructor() : this("", "","", "", "", "")
}

@Entity(tableName = "RoomBooking")
class RoomBookingEntity(
    @PrimaryKey val roomBookingID: String,
    val roomID: String,
    val tenantID: String,
    val checkInDate: String,
    val checkOutDate: String,
    val bookingStatus: String,
){
    constructor() : this("", "","", "", "", "")
}

data class RoomBookingWithTenantName(
    val roomBookingID: String,
    val roomID: String,
    val tenantID: String,
    val checkInDate: String,
    val checkOutDate: String,
    val bookingStatus: String,
    val tenantName: String // Added tenant name
){
    constructor() : this("", "","", "", "", "", "")
}

enum class RoomType{
    PRIVATE,
    SHARED
}

enum class BookingStatus{
    PENDING,
    CONFIRMED,
    CANCELLED
}

enum class RoomAvailability{
    AVAILABLE,
    UNAVAILABLE
}


