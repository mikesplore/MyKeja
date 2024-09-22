package com.mike.hms.homeScreen

import com.mike.hms.model.houseModel.HouseEntity


// House types
val houseTypes = listOf(
    HouseEntity(houseID = "1",houseName = "House1","Villa", houseImageLink = "https://cdn.shopify.com/s/files/1/0567/3873/files/ID_24402-2_480x480.jpg?v=1700459591", houseLocation = "Nairobi", houseRating = "5.0"),
    HouseEntity(houseID = "2","House2","Apartment", houseImageLink = "https://c8.alamy.com/comp/R8KC33/modern-residential-apartment-home-complex-block-with-outdoor-facilities-concept-R8KC33.jpg",houseLocation = "Mombasa", houseRating = "4.9"),
    HouseEntity(houseID = "3","House3","Hotel",houseImageLink ="https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/ed/95/07/limak-eurasia-luxury.jpg?w=700&h=-1&s=1",houseLocation ="Kilifi", houseRating = "4.6"),
    HouseEntity(houseID = "4","House4","Bungalow",houseImageLink ="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg/800px-George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg",houseLocation ="Kisumu"),
    HouseEntity(houseID = "5","House5","Condominiums",houseImageLink ="https://na.rdcpix.com/63bc3db5f65286a1fb4e2b1af9e14591w-c1980730521srd_q80.jpg",houseLocation ="Nairobi"),
    HouseEntity(houseID = "6","House6","Boutiques",houseImageLink ="https://newsouthhomes.com.au/wp-content/uploads/2021/10/1-6303-100-Castle-Hill-Road-WPH-22008-0301-c02-scaled.jpg",houseLocation ="Nairobi", houseRating = "4.8"),

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
    CarouselItem("Item 3", "https://cdn.lecollectionist.com/__lecollectionist__/production/houses/7194/photos/gb7CwF8US12864ThDGev_84abcf71-318a-42d5-8fac-9e6513a47b8a.jpg?width=2880&q=65")
)
