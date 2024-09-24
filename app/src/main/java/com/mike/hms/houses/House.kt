package com.mike.hms.houses

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseType
import java.util.Locale
import com.mike.hms.ui.theme.CommonComponents as CC

object HouseTypes {
    val houseTypes = listOf(
        HouseType.HOTEL,
        HouseType.APARTMENT,
        HouseType.BUNGALOW,
        HouseType.CONDOMINIUM,
        HouseType.VILLA
    )
}

@Composable
fun Houses(
    navController: NavController,
    context: Context
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<HouseType?>(null) }

    // Assume this function returns your list of houses
    val houseList = remember { houseTypes } // Replace with actual data fetching logic

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                textStyle = CC.contentTextStyle(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search houses...", style = CC.contentTextStyle()) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                colors = CC.outLinedTextFieldColors(),
                shape = CC.outLinedTextFieldShape()
            )

            // Filter chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedFilter == null, // "All" option when no filter is selected
                        onClick = { selectedFilter = null },
                        label = { Text("All", style = CC.contentTextStyle()) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                items(HouseTypes.houseTypes) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                filter.name.first().uppercase(Locale.getDefault()) + filter.name.substring(1).lowercase(Locale.getDefault()),
                                style = CC.contentTextStyle()
                            )
                        },
                        // Displaying enum name
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            // Filtered and searched houses
            val filteredHouses = houseList.filter { house ->
                // Filter by house type if selected, otherwise show all houses
                (selectedFilter == null || house.houseType == selectedFilter) &&
                        // Search by house name, type, or location
                        (house.houseName.contains(searchQuery, ignoreCase = true) ||
                                house.houseType.name.contains(searchQuery, ignoreCase = true) ||
                                house.houseLocation.contains(searchQuery, ignoreCase = true))
            }

            // House listings grouped by type
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val groupedHouses = filteredHouses.groupBy { it.houseType }
                groupedHouses.forEach { (type, housesOfType) ->
                    item {
                        Text(
                            text = type.name, // Use the enum name for the title
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(housesOfType) { house ->
                                HouseCard(
                                    house = house,
                                    onHouseClick = { navController.navigate("houseDetail/${house.houseID}") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun HouseCard(house: HouseEntity, onHouseClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardHeight = screenWidth * 0.5f
    val cardWidth = screenWidth * 0.49f

    val brush = Brush.horizontalGradient(
        colors = listOf(
            CC.primaryColor(),
            CC.tertiaryColor().copy(alpha = 0.5f)
        ),
    )
    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .padding(end = 16.dp, bottom = 16.dp)
            .clickable(onClick = onHouseClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = house.houseImageLink.random(),
                contentDescription = "House Image",
                modifier = Modifier
                    .height(cardHeight * 0.59f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(brush)
                    .padding(8.dp)
            ) {
                Text(
                    house.houseName,
                    style = CC.titleTextStyle()
                        .copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    house.houseLocation,
                    style = CC.contentTextStyle().copy(color = CC.extraPrimaryColor())
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "★ ${house.houseRating}",
                        style = CC.contentTextStyle().copy(
                            color = Color(0xFFFFA000)
                        )
                    )
                    Text(
                        house.rooms.size.toString() + " Rooms",
                        style = CC.contentTextStyle().copy(fontSize = 13.sp)
                    )
                }
            }
        }
    }
}


