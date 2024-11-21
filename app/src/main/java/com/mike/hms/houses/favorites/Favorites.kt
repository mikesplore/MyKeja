package com.mike.hms.houses.favorites

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.houses.formatNumber
import com.mike.hms.model.favorites.FavoriteViewModel
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favourites(navController: NavController, favoriteViewModel: FavoriteViewModel) {
    val favorites = favoriteViewModel.favorites.collectAsState(initial = emptyList())

    fun fetchFavoriteHouses() {
        favoriteViewModel.fetchFavoriteHouses()
    }

    fun deleteAllFavorites() {
        favoriteViewModel.clearAllFavorites()
    }

    LaunchedEffect(Unit) {
        fetchFavoriteHouses()
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My favorites", style = CC.titleTextStyle()) },
                actions = {
                    IconButton(
                        onClick = {
                            deleteAllFavorites()
                            fetchFavoriteHouses()
                        },
                        enabled = favorites.value.isNotEmpty(),
                        colors = IconButtonDefaults.iconButtonColors(

                        )
                    ) {
                        Icon(
                            Icons.Default.ClearAll,
                            contentDescription = "Clear All",
                            tint = CC.secondaryColor()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CC.primaryColor(),
                    titleContentColor = CC.secondaryColor()
                )
            )
        },
        containerColor = CC.primaryColor()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (favorites.value.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(text = "🥹")
                    Text(text = "No favorites yet", style = CC.contentTextStyle().copy(fontWeight = FontWeight.Bold))
                    OutlinedButton(
                        onClick = { navController.navigate("houses") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)

                    ) {
                        Text(text = "Browse Houses", style = CC.contentTextStyle())
                    }

                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites.value.size) { index ->
                    FavoriteHouseCard(
                        house = favorites.value[index],
                        navController = navController,
                        favoriteViewModel = favoriteViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteHouseCard(
    house: HouseEntity,
    favoriteViewModel: FavoriteViewModel,
    navController: NavController,
) {
    val configurations = LocalConfiguration.current
    val screenHeight = configurations.screenHeightDp.dp
    val screenWidth = configurations.screenWidthDp.dp

    // Calculate dynamic sizes based on screen dimensions
    val cardHeight = screenHeight * 0.18f
    val imageSize = cardHeight * 0.75f
    val contentPadding = screenWidth * 0.02f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = contentPadding, vertical = 8.dp)
            .height(cardHeight)
            .clickable { navController.navigate("houseDetails/${house.houseID}") },
        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 6.dp,
//            pressedElevation = 8.dp
//        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
//                .background(
//                    CC.primaryColor().copy(alpha = 0.5f),
//                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image Section
            Box(
                modifier = Modifier
                    .width(imageSize)
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = house.houseImageLink.firstOrNull(),
                    contentDescription = "House Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )

                // Rating Badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    color = CC.extraSecondaryColor(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = house.houseRating.toString(),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // Content Section
            Box(
                    modifier = Modifier.weight(1f)
            ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = house.houseName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = CC.secondaryColor(),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = house.houseLocation,
                            style = CC.contentTextStyle(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Price Tag
                    Text(
                        text = "Ksh ${formatNumber(house.housePrice)}/night",
                        style = CC.contentTextStyle().copy(fontSize = 14.sp)
                        )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // "Book Now" Button
                    Button(
                        onClick = { navController.navigate("houseDetails/${house.houseID}")},
                        colors =CC.buttonColors().copy(
                            contentColor = CC.primaryColor()
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Book Now",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Book Now",
                            style = CC.contentTextStyle().copy(
                                color = CC.primaryColor()
                            )
                        )
                    }
                }
            }
                //Delete Button
                IconButton(onClick = {},
                    modifier = Modifier
                        .align(Alignment.TopEnd)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = CC.secondaryColor(),
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
        }
    }
}

