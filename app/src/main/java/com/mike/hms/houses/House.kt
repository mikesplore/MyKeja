package com.mike.hms.houses

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mike.hms.model.getHouseViewModel
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
        HouseType.VILLA,
        HouseType.BOUTIQUE
    )
}

@Composable
fun Houses(
    navController: NavController,
    context: Context
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<HouseType?>(null) }
    val houseViewModel = getHouseViewModel(context)
    val houses by houseViewModel.houses.observeAsState()

    // Assume this function returns your list of houses
    val houseList = remember { houses } // Replace with actual data fetching logic

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
                placeholder = {
                    Text(
                        "Search by name, type, or location...",
                        style = CC.contentTextStyle()
                    )
                },
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
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CC.secondaryColor(),
                        ),
                        selected = selectedFilter == null, // "All" option when no filter is selected
                        onClick = { selectedFilter = null },
                        label = {
                            Text(
                                "All", style = CC.contentTextStyle().copy(
                                    color = if (selectedFilter == null) CC.primaryColor() else CC.textColor()
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                }
                items(HouseTypes.houseTypes) { filter ->
                    FilterChip(
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CC.secondaryColor(),
                        ),
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                filter.name.first()
                                    .uppercase(Locale.getDefault()) + filter.name.substring(1)
                                    .lowercase(Locale.getDefault()),
                                style = CC.contentTextStyle().copy(
                                    color = if (selectedFilter == filter) CC.primaryColor() else CC.textColor()
                                )
                            )
                        },
                        // Displaying enum name
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            // Filtered and searched houses
            val filteredHouses = houseList?.filter { house ->
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
                val groupedHouses = filteredHouses?.groupBy { it.houseType }
                groupedHouses?.forEach { (type, housesOfType) ->
                    item {
                        Text(
                            text = type.name
                                .first().uppercase(Locale.getDefault()) + type.name.substring(1)
                                .lowercase(Locale.getDefault())
                                .plus("s"), // Use the enum name for the title
                            style = CC.titleTextStyle(),
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
fun BookNow(house: HouseEntity) {
    // Define a small size for the card
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    val textSize = with(density) { (screenWidth * 0.05f).toSp() }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .background(
                color = CC.secondaryColor(),
                shape = RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = CC.secondaryColor()
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp), // Adjusted padding for more space
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            Text(
                text = "Ksh ${formatNumber(house.housePrice)} / night",
                style = CC.titleTextStyle().copy(
                    fontSize = textSize * 0.7f,
                    color = CC.extraSecondaryColor()
                )
            )

            Button(
                onClick = { /* Handle book now action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // Add space above the button
                colors = ButtonDefaults.buttonColors(
                    containerColor = CC.primaryColor(), // Set button background color
                    contentColor = Color.White // Set button text color
                ),
                shape = RoundedCornerShape(10.dp) // Rounded corners for the button
            ) {
                Text(
                    text = "Book Now",
                    style = CC.contentTextStyle().copy(
                        fontSize = textSize * 0.7f, // Adjust font size for the button
                        fontWeight = FontWeight.Bold // Make button text bold
                    )
                )
            }
        }
    }
}




