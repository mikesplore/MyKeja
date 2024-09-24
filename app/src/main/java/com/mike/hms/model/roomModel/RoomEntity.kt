package com.mike.hms.model.roomModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RoomTable")
class RoomEntity(
    @PrimaryKey val roomID: String,
    val roomNumber: String,
    val roomType: RoomType, // Added RoomType enum
    val roomCapacity: Int, // Changed to Int for capacity
    val roomAvailability: RoomAvailability, // Added enum for availability
    val amenities: Set<AmenityType>, // Set of amenities for this room
    val roomCategory: RoomCategory, // Added enum for room category
    val roomPrice: Int = 0, // Added room price
    val roomDescription: String = "", // Added room description
    val roomImageLink: List<String> = emptyList(), // Added room image link
    val roomRating: Double = 0.0, // Added room rating
){
    constructor() : this("", "", RoomType.SINGLE, 0, RoomAvailability.AVAILABLE, emptySet(), RoomCategory.STANDARD,0,"", emptyList())
}

@Entity(tableName = "FavouriteRooms")
class FavouriteRoomEntity(
    @PrimaryKey val roomID: String,
    val userID: String,
){
    constructor() : this("", "")
}



@Entity(tableName = "RoomAllocation")
class RoomAllocationEntity(
    @PrimaryKey val roomAllocationID: String,
    val roomID: String,
    val bedID: String,
    val tenantID: String,
    val checkInDate: Long, // Changed to Long for better date handling
    val checkOutDate: Long // Changed to Long for better date handling
){
    constructor() : this("", "", "", "", 0L, 0L)
}


@Entity(tableName = "RoomBooking")
class RoomBookingEntity(
    @PrimaryKey val roomBookingID: String,
    val roomID: String,
    val tenantID: String,
    val checkInDate: String,
    val checkOutDate: String,
    val bookingStatus: BookingStatus,
    val roomType: RoomType,
    val roomPrice: Double,
){
    constructor() : this("", "", "",
        "", "", BookingStatus.PENDING, RoomType.SINGLE,100.0)
}


data class RoomBookingWithTenantName(
    val roomBookingID: String,
    val roomID: String,
    val tenantID: String,
    val checkInDate: String,
    val checkOutDate: String,
    val bookingStatus: String,
    val firstName: String,
    val lastName: String,// Added tenant name
){
    constructor() : this("", "","", "", "", "", "", "")
}

enum class RoomType {
    SINGLE, DOUBLE, SUITE, FAMILY
}

enum class RoomCategory {
    ECONOMY, STANDARD, EXECUTIVE, DELUXE, LUXURY, FAMILY_SUITE
}

enum class RoomAvailability {
    AVAILABLE, OCCUPIED, RESERVED, OUT_OF_SERVICE, CLEANING, MAINTENANCE
}

enum class AmenityType {
    WIFI, TV, AIR_CONDITIONER, MINIBAR, COFFEE_MACHINE, SAFE, BALCONY, JACUZZI, HEATING
}

enum class BookingStatus {
    PENDING, CONFIRMED, CANCELLED, CHECKED_IN, NO_SHOW
}




