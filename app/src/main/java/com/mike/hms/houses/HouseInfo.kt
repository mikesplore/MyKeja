package com.mike.hms.houses

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.mike.hms.houses.houseComponents.HouseAmenities
import com.mike.hms.houses.houseComponents.InsideView
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.review.ReviewViewModel
import java.text.NumberFormat
import com.mike.hms.ui.theme.CommonComponents as CC


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HouseInfoScreen(navController: NavController, context: Context, houseID: String) {
    val houseViewModel: HouseViewModel = hiltViewModel()
    val reviewViewModel: ReviewViewModel = hiltViewModel()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val density = LocalDensity.current.density
    val textSize = (screenWidth.value / density).sp
    val isHouseFavorite = remember { mutableStateOf(false) }

    val house by houseViewModel.house.collectAsState()
    val reviews by reviewViewModel.reviews.collectAsState()
    val reviewsCount = reviews.size
    val reviewsAverage = reviews.map { it.review.rating }.average()
    val formattedAverage = "%.1f".format(reviewsAverage)
    val selectedImage = remember { mutableStateOf<String?>(null) }


    LaunchedEffect(houseID) {
        houseViewModel.getHouseByID(houseID)
        reviewViewModel.getReviewsByHouseId(houseID)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CC.primaryColor())
            .systemBarsPadding()
            .padding(bottom = 10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Gallery
        house?.let {
            HouseImageOverView(
                context = context,
                house = it,
                onImageClick = { imageUrl -> selectedImage.value = imageUrl },
                isHouseFavorite = isHouseFavorite,
                screenHeight = screenHeight,
                navController = navController
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // House Title
        house?.let { HouseTitleRow(house = it, textSize = textSize) }

        Spacer(modifier = Modifier.height(10.dp))

        // Location and Ratings
        house?.let { house -> HouseLocationAndRatings(house, reviewsCount.toString(), formattedAverage.toString(), navController) }

        Spacer(modifier = Modifier.height(10.dp))

        // Rooms and Availability
        house?.let { RoomsAndAvailability(it) }

        Spacer(modifier = Modifier.height(10.dp))

        // House Amenities
        house?.let { HouseAmenities(it) }
        Spacer(modifier = Modifier.height(10.dp))

        // House Description
        house?.let { HouseOverview(it) }
        Spacer(modifier = Modifier.height(10.dp))

        // Available Rooms
        InsideView(navController, context, houseID)
        Spacer(modifier = Modifier.height(10.dp))

        // Book Now
        house?.let { BookNow(it, navController) }
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
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .clickable{}
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
                "Chat with Host 💬",
                style = CC.contentTextStyle().copy(fontSize = textSize * 0.07f, color = CC.primaryColor()),
            )
        }
    }
}

fun formatNumber(number: Int): String {
    return NumberFormat.getNumberInstance().format(number)
}



