package com.mike.hms.model.houseModel

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "HouseTable")
data class HouseEntity(
    @PrimaryKey val houseID: String,
    val houseName: String = "",
    val houseType: HouseType = HouseType.HOTEL,                  // Type of house (e.g., villa, apartment)
    val houseLocation: String = "",              // General location or address
    val houseRating: String = "5.0",        // Rating of the house
    val houseImageLink: List<String> = emptyList(),             // Image link for the house
    val houseDescription: String = "",           // Description of the house
    val ownerID: String = "",                    // ID of the house owner: HouseOwner,                  // Link to the owner data class
    val bookingInfoID: String = "",           // Link to the booking information id
    val numberOfRooms: Int = 0,                  // Number of rooms in the house    ,                 // List of rooms id available in the house
    val housePrice: Int = 0,                // Price of the house or starting room price
    val houseReview: List<String> = emptyList(), // List of reviewIDs for the house
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




