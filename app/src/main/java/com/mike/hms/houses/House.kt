package com.mike.hms.houses

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
    }}
}




