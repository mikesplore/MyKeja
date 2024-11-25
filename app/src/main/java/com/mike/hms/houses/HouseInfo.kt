package com.mike.hms.houses

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
    configuration.screenHeightDp.dp
    val density = LocalDensity.current.density
    val textSize = (screenWidth.value / density).sp
    remember { mutableStateOf(false) }

    val house by houseViewModel.house.collectAsState()
    val reviews by reviewViewModel.reviews.collectAsState()
    val reviewsCount = reviews.size
    val reviewsAverage = reviews.map { it.review.rating }.average()
    val formattedAverage = if (reviews.isEmpty()) {
        "N/A" // Display 0.0 if there are no reviews
    } else {
        "%.1f".format(reviewsAverage) // Format to one decimal place otherwise
    }
    remember { mutableStateOf<String?>(null) }


    LaunchedEffect(houseID) {
        houseViewModel.getHouseByID(houseID)
        reviewViewModel.getReviewsByHouseId(houseID)
    }
    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.isStatusBarVisible = false
        onDispose {
            systemUiController.isStatusBarVisible = true
        }
    }

    ParallaxHeaderScreen(
        navController = navController,
        imageUrl = house?.houseImageLink?.firstOrNull().toString(),
        title = house?.houseName ?: "Loading"
    ) {
        // Your content here
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CC.primaryColor(), RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .systemBarsPadding()
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            house?.let { HouseTitleRow(house = it, textSize = textSize) }

            Spacer(modifier = Modifier.height(10.dp))

            // Location and Ratings
            house?.let { house ->
                HouseLocationAndRatings(
                    house,
                    reviewsCount.toString(),
                    formattedAverage.toString(),
                    navController
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Rooms and Availability
            house?.let { RoomsAndAvailability(it) }

            Spacer(modifier = Modifier.height(10.dp))

            // House Amenities
            house?.let {
                HouseAmenities(
                    it,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
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
                .clickable {}
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
                style = CC.contentTextStyle()
                    .copy(fontSize = textSize * 0.07f, color = CC.primaryColor()),
            )
        }
    }
}

fun formatNumber(number: Int): String {
    return NumberFormat.getNumberInstance().format(number)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallaxHeaderScreen(
    navController: NavController,
    imageUrl: String,
    title: String,
    content: @Composable () -> Unit
) {
    val scrollState = rememberLazyListState()
    val scrollOffset: Float by remember {
        derivedStateOf {
            scrollState.firstVisibleItemScrollOffset.toFloat().coerceAtMost(400f)
        }
    }

    // Calculate image height based on scroll
    val imageHeight = with(LocalDensity.current) {
        (400.dp - (scrollOffset / 2).toDp()).coerceAtLeast(100.dp)
    }

    // Calculate alpha for the top bar based on scroll
    val topBarAlpha = (scrollOffset / 400f).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CC.primaryColor())
    ) {
        // Background Image
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .align(Alignment.TopCenter)
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            CC.primaryColor().copy(alpha = 0.5f),
                            CC.primaryColor()
                        )
                    )
                )
        )

        // Content
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Spacer for the image
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }

            // Content section with rounded top corners
            item {
                Box(
                    modifier = Modifier

                        .fillMaxWidth()
                        .background(
                            CC.primaryColor(),
                            RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        )
                ) {
                    content()
                }
            }
        }

        // Top Bar
        TopAppBar(
            title = {
                AnimatedVisibility(
                    visible = topBarAlpha > 0.5f,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = title,
                        style = CC.titleTextStyle(),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .padding(4.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = CC.primaryColor().copy(alpha = topBarAlpha)
            ),
            modifier = Modifier.statusBarsPadding()
        )
    }
}


