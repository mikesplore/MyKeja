package com.mike.hms.homeScreen

import com.mike.hms.model.houseModel.HouseAmenities
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseType
import com.mike.hms.model.roomModel.AmenityType
import com.mike.hms.model.roomModel.RoomAvailability
import com.mike.hms.model.roomModel.RoomCategory
import com.mike.hms.model.roomModel.RoomEntity
import com.mike.hms.model.roomModel.RoomType


// House types
val houseTypes = listOf(
    HouseEntity(houseID = "1", "Riverside Villa", HouseType.VILLA, houseImageLink = listOf("https://cdn.shopify.com/s/files/1/0567/3873/files/ID_24402-2_480x480.jpg?v=1700459591","https://cdn.shopify.com/s/files/1/0567/3873/files/ID_24402-2_480x480.jpg?v=1700459591"), houseLocation = "Nairobi", houseRating = "4.8", housePrice = 20000.0f, houseAmenities = listOf(HouseAmenities.GARDEN, HouseAmenities.JACUZZI, HouseAmenities.SWIMMING_POOL, HouseAmenities.OUTDOOR, HouseAmenities.PARKING, HouseAmenities.FIREPLACE), rooms = listOf("1","2","3")),
    HouseEntity(houseID = "2", "Palm Breeze Apartments", HouseType.APARTMENT, houseImageLink = listOf("https://kenyahomes.co.ke/blog/wp-content/uploads/2019/04/florida-3720056__340.jpg", "https://kenyahomes.co.ke/blog/wp-content/uploads/2019/04/florida-3720056__340.jpg"), houseLocation = "Mombasa", houseRating = "4.6"),
    HouseEntity(houseID = "3", "Seaside Resort", HouseType.HOTEL, houseImageLink = listOf("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/ed/95/07/limak-eurasia-luxury.jpg?w=700&h=-1&s=1"), houseLocation = "Kilifi", houseRating = "4.7"),
    HouseEntity(houseID = "4", "Lakeview Bungalow", HouseType.BUNGALOW, houseImageLink = listOf("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg/800px-George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg"), houseLocation = "Kisumu", houseRating = "4.5"),
    HouseEntity(houseID = "5", "Nairobi Heights", HouseType.CONDOMINIUM, houseImageLink = listOf("https://na.rdcpix.com/63bc3db5f65286a1fb4e2b1af9e14591w-c1980730521srd_q80.jpg"), houseLocation = "Nairobi", houseRating = "4.9"),
    HouseEntity(houseID = "6", "Boutique Haven", HouseType.BOUTIQUE, houseImageLink = listOf("https://newsouthhomes.com.au/wp-content/uploads/2021/10/1-6303-100-Castle-Hill-Road-WPH-22008-0301-c02-scaled.jpg"), houseLocation = "Nairobi", houseRating = "4.8"),
    HouseEntity(houseID = "1", "Riverside Villa", HouseType.VILLA, houseImageLink = listOf("https://cdn.shopify.com/s/files/1/0567/3873/files/ID_24402-2_480x480.jpg?v=1700459591"), houseLocation = "Nairobi", houseRating = "4.8"),
    HouseEntity(houseID = "2", "Palm Breeze Apartments", HouseType.APARTMENT, houseImageLink = listOf("https://kenyahomes.co.ke/blog/wp-content/uploads/2019/04/florida-3720056__340.jpg", "https://kenyahomes.co.ke/blog/wp-content/uploads/2019/04/florida-3720056__340.jpg"), houseLocation = "Mombasa", houseRating = "4.6"),
    HouseEntity(houseID = "3", "Seaside Resort", HouseType.HOTEL, houseImageLink = listOf("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/ed/95/07/limak-eurasia-luxury.jpg?w=700&h=-1&s=1"), houseLocation = "Kilifi", houseRating = "4.7"),
    HouseEntity(houseID = "4", "Lakeview Bungalow", HouseType.BUNGALOW, houseImageLink = listOf("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg/800px-George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg"), houseLocation = "Kisumu", houseRating = "4.5"),
    HouseEntity(houseID = "5", "Nairobi Heights", HouseType.CONDOMINIUM, houseImageLink = listOf("https://na.rdcpix.com/63bc3db5f65286a1fb4e2b1af9e14591w-c1980730521srd_q80.jpg"), houseLocation = "Nairobi", houseRating = "4.9"),
    HouseEntity(houseID = "6", "Boutique Haven", HouseType.BOUTIQUE, houseImageLink = listOf("https://newsouthhomes.com.au/wp-content/uploads/2021/10/1-6303-100-Castle-Hill-Road-WPH-22008-0301-c02-scaled.jpg"), houseLocation = "Nairobi", houseRating = "4.8"),
    HouseEntity(houseID = "1", "Riverside Villa", HouseType.VILLA, houseImageLink = listOf("https://cdn.shopify.com/s/files/1/0567/3873/files/ID_24402-2_480x480.jpg?v=1700459591"), houseLocation = "Nairobi", houseRating = "4.8"),
    HouseEntity(houseID = "2", "Palm Breeze Apartments", HouseType.APARTMENT, houseImageLink = listOf("https://kenyahomes.co.ke/blog/wp-content/uploads/2019/04/florida-3720056__340.jpg", "https://kenyahomes.co.ke/blog/wp-content/uploads/2019/04/florida-3720056__340.jpg"), houseLocation = "Mombasa", houseRating = "4.6"),
    HouseEntity(houseID = "3", "Seaside Resort", HouseType.HOTEL, houseImageLink = listOf("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/ed/95/07/limak-eurasia-luxury.jpg?w=700&h=-1&s=1"), houseLocation = "Kilifi", houseRating = "4.7"),
    HouseEntity(houseID = "4", "Lakeview Bungalow", HouseType.BUNGALOW, houseImageLink = listOf("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg/800px-George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg"), houseLocation = "Kisumu", houseRating = "4.5"),
    HouseEntity(houseID = "5", "Nairobi Heights", HouseType.CONDOMINIUM, houseImageLink = listOf("https://na.rdcpix.com/63bc3db5f65286a1fb4e2b1af9e14591w-c1980730521srd_q80.jpg"), houseLocation = "Nairobi", houseRating = "4.9"),
    HouseEntity(houseID = "6", "Boutique Haven", HouseType.BOUTIQUE, houseImageLink = listOf("https://newsouthhomes.com.au/wp-content/uploads/2021/10/1-6303-100-Castle-Hill-Road-WPH-22008-0301-c02-scaled.jpg"), houseLocation = "Nairobi", houseRating = "4.8")

)



// Mock data class
data class CarouselItem(
    val itemName: String,
    val itemImageLink: String
)

// Mock data list
val carouselItems = listOf(
    CarouselItem("Item 1", "https://img.freepik.com/free-vector/flat-hotel-facade-background_23-2148157379.jpg"),
    CarouselItem("Item 2", "https://na.rdcpix.com/90885237/8ecb8b5c10a19ccbe1daba7bc38ec77cw-c303855rd-w832_h468_r4_q80.jpg"),
    CarouselItem("Item 3", "https://cdn.lecollectionist.com/__lecollectionist__/production/houses/7194/photos/gb7CwF8US12864ThDGev_84abcf71-318a-42d5-8fac-9e6513a47b8a.jpg?width=2880&q=65"),

)


//Mock rooms
val rooms = listOf(
    RoomEntity(
        roomID = "R001",
        roomNumber = "101",
        roomType = RoomType.SINGLE,
        roomCapacity = 1,
        roomAvailability = RoomAvailability.AVAILABLE,
        amenities = setOf(AmenityType.WIFI, AmenityType.TV, AmenityType.AIR_CONDITIONER),
        roomCategory = RoomCategory.STANDARD,
        roomPrice = 1000,
        roomDescription = "Cozy single room with a view",
        roomImageLink = listOf("https://webbox.imgix.net/images/owvecfmxulwbfvxm/c56a0c0d-8454-431a-9b3e-f420c72e82e3.jpg?auto=format,compress&fit=crop&crop=entropy")

    ),
    RoomEntity(
        roomID = "R002",
        roomNumber = "102",
        roomType = RoomType.DOUBLE,
        roomCapacity = 2,
        roomAvailability = RoomAvailability.OCCUPIED,
        amenities = setOf(AmenityType.WIFI, AmenityType.MINIBAR, AmenityType.BALCONY),
        roomCategory = RoomCategory.FAMILY_SUITE,
        roomPrice = 1500,
        roomDescription = "Spacious double room with a balcony",
        roomImageLink = listOf("https://cms.saharalasvegas.com/wp-content/uploads/2022/03/Marra-Style-Featured-Image-1048-%C3%97-640-px-1024x625.jpg")
    ),
    RoomEntity(
        roomID = "R003",
        roomNumber = "103",
        roomType = RoomType.SUITE,
        roomCapacity = 4,
        roomAvailability = RoomAvailability.RESERVED,
        amenities = setOf(AmenityType.WIFI, AmenityType.TV, AmenityType.COFFEE_MACHINE, AmenityType.BALCONY),
        roomCategory = RoomCategory.LUXURY,
        roomPrice = 20000,
        roomDescription = "Luxurious suite with a view",
        roomImageLink = listOf("https://sunnsand.co.ke/wp-content/uploads/2023/06/SP_6450-HDR.jpg")
    ),
    RoomEntity(
        roomID = "R004",
        roomNumber = "104",
        roomType = RoomType.FAMILY,
        roomCapacity = 3,
        roomAvailability = RoomAvailability.AVAILABLE,
        amenities = setOf(AmenityType.WIFI, AmenityType.TV, AmenityType.MINIBAR, AmenityType.SAFE),
        roomCategory = RoomCategory.LUXURY,
        roomPrice = 5800,
        roomDescription = "Cozy family room with a safe",
        roomImageLink = listOf("https://watermark.lovepik.com/photo/20211120/large/lovepik-hotel-rooms-picture_500420407.jpg")
    ),
    RoomEntity(
        roomID = "R005",
        roomNumber = "105",
        roomType = RoomType.FAMILY,
        roomCapacity = 6,
        roomAvailability = RoomAvailability.OCCUPIED,
        amenities = setOf(AmenityType.WIFI, AmenityType.TV, AmenityType.AIR_CONDITIONER, AmenityType.MINIBAR),
        roomCategory = RoomCategory.FAMILY_SUITE,
        roomPrice = 25000,
        roomDescription = "Spacious family room with a view",
        roomImageLink = listOf("https://t3.ftcdn.net/jpg/02/94/19/40/360_F_294194023_disE35GtlVLDQx4caNDaWewZI8LbxWFQ.jpg")
    ),
    RoomEntity(
        roomID = "R006",
        roomNumber = "106",
        roomType = RoomType.SINGLE,
        roomCapacity = 1,
        roomAvailability = RoomAvailability.OUT_OF_SERVICE,
        amenities = setOf(AmenityType.WIFI, AmenityType.COFFEE_MACHINE),
        roomCategory = RoomCategory.ECONOMY,
        roomPrice = 500,
        roomDescription = "Cozy single room with a coffee machine",
        roomImageLink = listOf("https://foyr.com/learn/wp-content/uploads/2024/02/Guest-Room.jpg")
    ),
    RoomEntity(
        roomID = "R007",
        roomNumber = "107",
        roomType = RoomType.DOUBLE,
        roomCapacity = 2,
        roomAvailability = RoomAvailability.AVAILABLE,
        amenities = setOf(AmenityType.WIFI, AmenityType.AIR_CONDITIONER, AmenityType.MINIBAR, AmenityType.BALCONY),
        roomCategory = RoomCategory.ECONOMY,
        roomPrice = 1200,
        roomDescription = "Cozy double room with a balcony",
        roomImageLink = listOf("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcWWu9_WUImovRROEaM_VZsAQn8Cpnm9kovg&s")
    ),
    RoomEntity(
        roomID = "R008",
        roomNumber = "108",
        roomType = RoomType.SUITE,
        roomCapacity = 4,
        roomAvailability = RoomAvailability.OCCUPIED,
        amenities = setOf(AmenityType.WIFI, AmenityType.TV, AmenityType.MINIBAR, AmenityType.SAFE, AmenityType.COFFEE_MACHINE),
        roomCategory = RoomCategory.LUXURY,
        roomPrice = 8200,
        roomDescription = "Luxurious suite with a safe and a coffee machine",
        roomImageLink = listOf("https://t3.ftcdn.net/jpg/06/19/00/08/360_F_619000872_AxiwLsfQqRHMkNxAbN4l5wg1MsPgBsmo.jpg")
    ),
    RoomEntity(
        roomID = "R009",
        roomNumber = "109",
        roomType = RoomType.FAMILY,
        roomCapacity = 5,
        roomAvailability = RoomAvailability.AVAILABLE,
        amenities = setOf(AmenityType.WIFI, AmenityType.TV, AmenityType.MINIBAR, AmenityType.BALCONY),
        roomCategory = RoomCategory.EXECUTIVE,
        roomPrice = 19000,
        roomDescription = "Cozy family room with a balcony",
        roomImageLink = listOf("https://hydehotels.com/wp-content/uploads/sites/4/2024/05/hydebodrum-bed-beigeheadboard-creamseat.jpg")
    ),
    RoomEntity(
        roomID = "R010",
        roomNumber = "110",
        roomType = RoomType.DOUBLE,
        roomCapacity = 3,
        roomAvailability = RoomAvailability.RESERVED,
        amenities = setOf(AmenityType.WIFI, AmenityType.TV, AmenityType.AIR_CONDITIONER, AmenityType.SAFE),
        roomCategory = RoomCategory.LUXURY,
        roomPrice = 16000,
        roomDescription = "Cozy double room with a safe",
        roomImageLink = listOf("https://cdn.prod.website-files.com/5c6d6c45eaa55f57c6367749/65045f093c166fdddb4a94a5_x-65045f0266217.webp")
    )
)
