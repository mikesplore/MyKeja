package com.mike.hms.houses.addOrEditHouse

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mike.hms.model.houseModel.HouseAmenities
import com.mike.hms.model.houseModel.HouseCategory
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseType
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.ui.theme.CommonComponents as CC


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseForm(context: Context) {
    var houseName by remember { mutableStateOf("") }
    var houseLocation by remember { mutableStateOf("") }
    var houseRating by remember { mutableStateOf("5.0") }
    var housePrice by remember { mutableStateOf("0") }
    var numberOfRooms by remember { mutableStateOf("0") }
    var houseCapacity by remember { mutableStateOf("0") }
    var houseDescription by remember { mutableStateOf("") }
    var selectedHouseType by remember { mutableStateOf<HouseType?>(null) }
    var selectedHouseCategory by remember { mutableStateOf<HouseCategory?>(null) }
    var selectedHouseAmenities by remember { mutableStateOf(setOf<HouseAmenities>()) }
    var imageLinks by remember { mutableStateOf(listOf<String>()) }
    var currentImageLink by remember { mutableStateOf("") }

    val houseViewModel: HouseViewModel = hiltViewModel()
    val onSubmit: (houseEntity: HouseEntity, onComplete: (Boolean) -> Unit) -> Unit =
        { house, onComplete -> houseViewModel.insertHouse(house) { complete -> onComplete(complete) } }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add House", style = CC.titleTextStyle()) },
                colors = CC.topAppBarColors().copy(
                    containerColor = CC.extraSecondaryColor(),
                    titleContentColor = CC.textColor()
                )
            )
        },
        containerColor = CC.primaryColor()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .imePadding()
                .padding(innerPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
            Text(
                "House Details",
                style = CC.titleTextStyle().copy(color = CC.tertiaryColor()),
            )}

            HouseNameAndLocation(
                houseName,
                { houseName = it },
                houseLocation,
                { houseLocation = it })

            //Rooms and Capacity
            HouseRoomsAndCapacity(
                numberOfRooms,
                { numberOfRooms = it },
                houseCapacity,
                { houseCapacity = it })

            // House Price and Rating
            HousePriceAndRatings(housePrice, { housePrice = it }, houseRating, { houseRating = it })

            // House Description
            HouseDescription(houseDescription) { houseDescription = it }

            // House Type
            HouseTypeSelection(selectedHouseType) { selectedHouseType = it }

            // House Category
            HouseCategorySelection(selectedHouseCategory) { selectedHouseCategory = it }

            // House Amenities
            HouseAmenitiesSelection(selectedHouseAmenities.toList()) { amenity ->

                selectedHouseAmenities = if (selectedHouseAmenities.contains(amenity)) {
                    selectedHouseAmenities - amenity
                } else {
                    selectedHouseAmenities + amenity
                }
            }


            // Image Links
            HouseImages(
                imageLinks = imageLinks,
                onImageLinksChange = { imageLinks = it },
                currentImageLink = currentImageLink,
                onCurrentImageLinkChange = { currentImageLink = it }
            )

            Button(
                onClick = {
                    if (houseName.isEmpty() || houseLocation.isEmpty() || selectedHouseType == null || selectedHouseCategory == null) {
                        Toast.makeText(
                            context,
                            "Please fill in all required fields",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    CC.generateHouseId { id ->
                        val houseEntity = HouseEntity(
                            houseID = id,
                            houseName = houseName,
                            houseType = selectedHouseType ?: HouseType.HOTEL,
                            houseLocation = houseLocation,
                            houseRating = houseRating,
                            houseImageLink = imageLinks,
                            houseDescription = houseDescription,
                            ownerID = "",
                            bookingInfoID = "",
                            numberOfRooms = numberOfRooms.toIntOrNull() ?: 0,
                            housePrice = housePrice.toIntOrNull() ?: 0,
                            houseReview = emptyList(),
                            houseAmenities = selectedHouseAmenities.toList(),
                            houseAvailable = true,
                            houseCategory = selectedHouseCategory ?: HouseCategory.ECONOMY,
                            houseCapacity = houseCapacity.toIntOrNull() ?: 0
                        )
                        onSubmit(houseEntity) { success ->
                            if (success) {
                                // Reset form fields
                                houseName = ""
                                houseLocation = ""
                                houseRating = "5.0"
                                housePrice = "0"
                                numberOfRooms = "0"
                                houseCapacity = "0"
                                houseDescription = ""
                                selectedHouseType = null
                                selectedHouseCategory = null
                                selectedHouseAmenities = emptySet()
                                imageLinks = listOf()
                                currentImageLink = ""
                                Log.d("HouseForm", "House added successfully")
                            } else {
                                Log.d("HouseForm", "Failed to add house")
                            }
                        }
                    }


                },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.9f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CC.secondaryColor(),
                    contentColor = CC.extraSecondaryColor()
                ),
                shape = CC.outLinedTextFieldShape()
            ) {
                Text("Add House", style = CC.bodyTextStyle().copy(color = CC.primaryColor()))
            }
        }
    }
}