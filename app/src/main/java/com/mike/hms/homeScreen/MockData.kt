package com.mike.hms.homeScreen

import com.mike.hms.model.houseModel.HouseEntity


// House types
val houseTypes = listOf(
    HouseEntity(houseID = "1","House1","Villa","https://cdn.shopify.com/s/files/1/0567/3873/files/ID_24402-2_480x480.jpg?v=1700459591","Nairobi"),
    HouseEntity(houseID = "2","House2","Apartment","https://c8.alamy.com/comp/R8KC33/modern-residential-apartment-home-complex-block-with-outdoor-facilities-concept-R8KC33.jpg","Mombasa"),
    HouseEntity(houseID = "1","House1","Hotel","https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/ed/95/07/limak-eurasia-luxury.jpg?w=700&h=-1&s=1","Nairobi"),
    HouseEntity(houseID = "2","House2","Bungalow","https://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg/800px-George_L._Burlingame_House%2C_1238_Harvard_St%2C_Houston_%28HDR%29.jpg","Mombasa"),

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
