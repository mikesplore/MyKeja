package com.mike.hms.model.houseModel

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "HouseTable")
data class HouseEntity(
    @PrimaryKey val houseID: String,
    val houseName: String = "",
    val houseType: HouseType = HouseType.HOTEL,
    val houseLocation: String = "",
    val houseRating: String = "5.0",
    val houseImageLink: List<String> = emptyList(),
    val houseDescription: String = "",
    val ownerID: String = "",
    val bookingInfoID: String = "",
    val numberOfRooms: Int = 0,
    val housePrice: Int = 0,
    val houseReview: List<String> = emptyList(),
    val houseAmenities: List<HouseAmenities> = emptyList(),
    val houseAvailable: Boolean = true,
    val houseCategory: HouseCategory = HouseCategory.ECONOMY,
    val houseCapacity: Int = 0,

){
    constructor() : this("","",  HouseType.HOTEL, "", "", emptyList(), "", "", "", 0, 0, emptyList(), emptyList(), true)
}

data class HouseOwner(
    val ownerName: String,                  // Name of the house owner
    val ownerContact: String                // Contact info (email or phone)
){
    constructor() : this("", "")
}

data class BookingInfo(
    val bookingID: String,                  // ID of the booking
    val housePrice: String,                 // Price of booking the house or starting room price
    val houseAvailable: Boolean,            // Availability status (true/false)
    val visitCount: Int = 0,                // Number of visits or views for the house
    val bookingPolicy: String               // Booking or cancellation policy
){
    constructor() : this("", "", false, 0, "")
}

enum class HouseType {
    VILLA,
    APARTMENT,
    HOTEL,
    BUNGALOW,
    CONDOMINIUM,
    BOUTIQUE
}

enum class  HouseAmenities{
    SWIMMING_POOL,
    PARKING,
    OUTDOOR,
    JACUZZI,
    FIREPLACE,
    SAUNA,
    BEACHFRONT,
    GARDEN,
    WATERFRONT,
    MOUNTAIN_VIEW,
}

enum class HouseCategory{
    ECONOMY,
    STANDARD,
    FAMILY_SUITE,
    LUXURY,
    DELUXE,
}




