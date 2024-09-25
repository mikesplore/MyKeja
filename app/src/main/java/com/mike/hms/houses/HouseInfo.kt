package com.mike.hms.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.model.houseModel.HouseEntity
import java.text.NumberFormat
import com.mike.hms.ui.theme.CommonComponents as CC


@Composable
fun HouseDetailScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val density = LocalDensity.current.density
    val textSize = (screenWidth.value / density).sp
    val isHouseFavorite = remember { mutableStateOf(false) }
    val house = houseTypes[0]
    var selectedImage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .background(CC.primaryColor())
            .padding(top = 16.dp, bottom = 10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Gallery
        HouseImageOverView(
            house = house,
            onImageClick = { selectedImage = it },
            isHouseFavorite = isHouseFavorite,
            screenHeight = screenHeight
        )

        Spacer(modifier = Modifier.height(10.dp))

        // House Title
        HouseTitleRow(house = house, textSize = textSize)

        Spacer(modifier = Modifier.height(10.dp))

        // Location and Ratings
        HouseLocationAndRatings(house, navController)

        Spacer(modifier = Modifier.height(10.dp))

        // Rooms and Availability
        RoomsAndAvailability(house)

        Spacer(modifier = Modifier.height(10.dp))

        // House Amenities
        HouseAmenities(house)
        Spacer(modifier = Modifier.height(10.dp))

        // House Description
        HouseDescription(house)
        Spacer(modifier = Modifier.height(10.dp))

        // Available Rooms
        AvailableRooms(navController)
        Spacer(modifier = Modifier.height(10.dp))

        // Book Now
        BookNow(house)
    }

}


@Composable
fun HouseTitleRow(house: HouseEntity, textSize: TextUnit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = house.houseName,
            style = CC.titleTextStyle().copy(fontSize = textSize * 0.15f),
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = CC.secondaryColor(),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = CC.secondaryColor(),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Ksh ${formatNumber(house.housePrice)}",
                style = CC.titleTextStyle().copy(
                    fontSize = textSize * 0.08f,
                    color = CC.primaryColor()
                )
            )
        }
    }
}

fun formatNumber(number: Int): String {
    return NumberFormat.getNumberInstance().format(number)
}



