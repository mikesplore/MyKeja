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
    val roomCategory: RoomCategory // Added enum for room category
){
    constructor() : this("", "", RoomType.SHARED, 0, RoomAvailability.AVAILABLE, emptySet(), RoomCategory.STANDARD)
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
    val checkInDate: Long, // Changed to Long for better date handling
    val checkOutDate: Long, // Changed to Long for better date handling
    val bookingStatus: BookingStatus,
    val roomType: RoomType // Include room type
){
    constructor() : this("", "", "", 0L, 0L, BookingStatus.PENDING, RoomType.SHARED)
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

val roomAmenities: Set<AmenityType> = setOf(
    AmenityType.BASIC,
    AmenityType.COMFORT,
    AmenityType.ENTERTAINMENT,
    AmenityType.SPECIAL,
    AmenityType.SAFETY,
    AmenityType.BUSINESS,
    AmenityType.EXTRA
)


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

enum class RoomCategory{
    SMALL,
    STANDARD,
    FAMILY,
    SINGLE,
    EXECUTIVE,
    DELUXE,
    RESIDENTIAL,
    SUITE
}



enum class AmenityType {
    BASIC,
    ENTERTAINMENT,
    COMFORT,
    SPECIAL,
    SAFETY,
    BUSINESS,
    EXTRA
}

enum class BasicAmenity {
    WIFI,
    AIR_CONDITIONING,
    HEATING,
    POWER_OUTLETS,
    WORKSPACE,
    PRIVATE_BATHROOM,
    TOWELS_TOILETRIES,
    ROOM_CLEANING,
    SAFE
}


enum class EntertainmentAmenity {
    TV,
    BLUETOOTH_SPEAKERS,
    GAMING_CONSOLE,
    SATELLITE_TV
}


enum class ComfortAmenity {
    MINIBAR,
    COFFEE_MAKER,
    ROOM_SERVICE,
    IRON_IRONING_BOARD,
    WARDROBE,
    HAIR_DRYER
}


enum class SpecialAmenity {
    BALCONY,
    SCENIC_VIEW,
    KITCHENETTE,
    BATHTUB,
    SOUNDPROOFING,
    ACCESSIBILITY_FEATURES
}


enum class SafetyAmenity {
    SMOKE_DETECTOR,
    FIRE_EXTINGUISHER,
    SECURITY_CAMERA,
    KEYCARD_ACCESS
}


enum class BusinessAmenity {
    MEETING_ROOM,
    PRINTER_SCANNER_FAX
}

enum class ExtraServiceAmenity {
    LAUNDRY_SERVICE,
    TRANSPORTATION,
    PARKING
}



