package com.mike.hms.model.houseModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mike.hms.model.roomModel.RoomEntity

@Entity(tableName = "HouseTable")
data class HouseEntity(
    @PrimaryKey val houseID: String,
    val houseName: String = "",
    val houseType: String = "",                  // Type of house (e.g., villa, apartment)
    val houseLocation: String = "",              // General location or address
    val houseRating: String = "5.0",        // Rating of the house
    val houseImageLink: String = "",             // Image link for the house
    val houseDescription: String = "",           // Description of the house
    val ownerID: String = "",                    // ID of the house owner: HouseOwner,                  // Link to the owner data class
    val bookingInfoID: String = "",           // Link to the booking information
    val rooms: List<String>   = listOf()                    // List of rooms id available in the house
)

data class HouseOwner(
    val ownerName: String,                  // Name of the house owner
    val ownerContact: String                // Contact info (email or phone)
)

data class BookingInfo(
    val bookingID: String,                  // ID of the booking
    val housePrice: String,                 // Price of booking the house or starting room price
    val houseAvailable: Boolean,            // Availability status (true/false)
    val visitCount: Int = 0,                // Number of visits or views for the house
    val bookingPolicy: String               // Booking or cancellation policy
)




