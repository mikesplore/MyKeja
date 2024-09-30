package com.mike.hms.houses

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mike.hms.model.getHouseViewModel
import com.mike.hms.model.houseModel.HouseAmenities
import com.mike.hms.model.houseModel.HouseCategory
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseType
import com.mike.hms.ui.theme.CommonComponents as CC


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HouseForm(context: Context) {
    var houseName by remember { mutableStateOf("") }
    var houseLocation by remember { mutableStateOf("") }
    var houseRating by remember { mutableStateOf("5.0") }
    var housePrice by remember { mutableStateOf("0") }
    var numberOfRooms by remember { mutableStateOf("0") }
    var houseCapacity by remember { mutableStateOf("0") }
    var houseDescription by remember { mutableStateOf("") }
    var selectedHouseTypes by remember { mutableStateOf(setOf<HouseType>()) }
    var selectedHouseCategories by remember { mutableStateOf(setOf<HouseCategory>()) }
    var selectedHouseAmenities by remember { mutableStateOf(setOf<HouseAmenities>()) }
    var imageLinks by remember { mutableStateOf(listOf<String>()) }
    var currentImageLink by remember { mutableStateOf("") }

    val houseViewModel = getHouseViewModel(context)
    val onSubmit: (houseEntity: HouseEntity, onComplete: (Boolean) -> Unit) -> Unit =
        { house, onComplete -> houseViewModel.insertHouse(house) { complete -> onComplete(complete) } }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add House", style = CC.titleTextStyle()) },
                colors = CC.topAppBarColors()
            )
        },
        containerColor = CC.primaryColor()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "House Details",
                style = CC.titleTextStyle().copy(fontStyle = FontStyle.Italic),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HouseNameAndLocation(houseName, { houseName = it }, houseLocation, { houseLocation = it })

            //Rooms and Capacity
            HouseRoomsAndCapacity(numberOfRooms, { numberOfRooms = it }, houseCapacity, { houseCapacity = it })

            // House Price and Rating
            HousePriceAndRatings(housePrice, { housePrice = it }, houseRating, { houseRating = it })

            // House Description
            HouseDescription(houseDescription, { houseDescription = it })

            Text(
                "House Types",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(0.9f),

            ) {
                HouseType.entries.forEach { houseType ->
                    FilterChip(
                        selected = houseType in selectedHouseTypes,
                        onClick = {
                            selectedHouseTypes = if (houseType in selectedHouseTypes) {
                                selectedHouseTypes - houseType
                            } else {
                                selectedHouseTypes + houseType
                            }
                        },
                        label = { Text(houseType.name) }
                    )
                }
            }

            Text(
                "House Categories",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(0.9f),

            ) {
                HouseCategory.entries.forEach { houseCategory ->
                    FilterChip(
                        selected = houseCategory in selectedHouseCategories,
                        onClick = {
                            selectedHouseCategories = if (houseCategory in selectedHouseCategories) {
                                selectedHouseCategories - houseCategory
                            } else {
                                selectedHouseCategories + houseCategory
                            }
                        },
                        label = { Text(houseCategory.name) }
                    )
                }
            }

            Text(
                "Image Links",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CC.MyOutlinedTextField(
                    value = currentImageLink,
                    onValueChange = { currentImageLink = it },
                    placeholder = "Enter Image Link",
                    singleLine = true,
                    label = "Image Link",
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        if (currentImageLink.isNotEmpty()) {
                            imageLinks = imageLinks + currentImageLink
                            currentImageLink = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Image")
                }
            }

            if (imageLinks.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(imageLinks) { link ->
                        InputChip(
                            selected = false,
                            onClick = {},
                            label = { Text(link, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remove",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (houseName.isEmpty() || houseLocation.isEmpty() || selectedHouseTypes.isEmpty() || selectedHouseCategories.isEmpty()) {
                        Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val houseEntity = HouseEntity(
                        houseID = "",
                        houseName = houseName,
                        houseType = selectedHouseTypes.firstOrNull() ?: HouseType.HOTEL,
                        houseLocation = houseLocation,
                        houseRating = houseRating,
                        houseImageLink = imageLinks,
                        houseDescription = houseDescription,
                        ownerID = "",
                        bookingInfoID = "",
                        numberOfRooms = numberOfRooms.toIntOrNull() ?: 0,
                        housePrice = housePrice.toIntOrNull() ?: 0,
                        houseReview = emptyList(),
                        houseAmenities = emptyList(),
                        houseAvailable = true,
                        houseCategory = selectedHouseCategories.firstOrNull() ?: HouseCategory.ECONOMY,
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
                            selectedHouseTypes = setOf()
                            selectedHouseCategories = setOf()
                            imageLinks = listOf()
                            currentImageLink = ""
                            Toast.makeText(context, "House added successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to add house", Toast.LENGTH_SHORT).show()
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
                Text("Submit", style = CC.bodyTextStyle().copy(color = CC.primaryColor()))
            }
        }
    }
}


@Composable
fun HouseNameAndLocation(
    houseName: String,
    onHouseNameChange: (String) -> Unit,
    houseLocation: String,
    onHouseLocationChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CC.MyOutlinedTextField(
            value = houseName,
            onValueChange = onHouseNameChange, // Pass the callback
            label = "House Name",
            placeholder = "Enter House Name",
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        CC.MyOutlinedTextField(
            value = houseLocation,
            onValueChange = onHouseLocationChange, // Pass the callback
            label = "Location",
            placeholder = "",
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
    }
}


@Composable
fun HousePriceAndRatings(
    housePrice: String,
    onHousePriceChange: (String) -> Unit,
    houseRating: String,
    onHouseRatingChange: (String) -> Unit

){
    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CC.MyOutlinedTextField(
            value = housePrice,
            onValueChange = { onHousePriceChange(it) },
            label = "Price",
            placeholder = "House Price",
            singleLine = true,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        CC.MyOutlinedTextField(
            value = houseRating,
            onValueChange = { onHouseRatingChange(it)},
            label = "Rating",
            placeholder = "House Rating",
            singleLine = true,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun HouseRoomsAndCapacity(
    numberOfRooms: String,
    onNumberOfRoomsChange: (String) -> Unit,
    houseCapacity: String,
    onHouseCapacityChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CC.MyOutlinedTextField(
            value = numberOfRooms,
            onValueChange = { onNumberOfRoomsChange(it) },
            label = "Rooms",
            placeholder = "Number of Rooms",
            singleLine = true,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        CC.MyOutlinedTextField(
            value = houseCapacity,
            onValueChange = { onHouseCapacityChange(it) },
            label = "Capacity",
            placeholder = "House Capacity",
            singleLine = true,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun HouseDescription(
    houseDescription: String,
    onHouseDescriptionChange: (String) -> Unit

){
    CC.MyOutlinedTextField(
        value = houseDescription,
        onValueChange = { onHouseDescriptionChange(it)},
        label = "Description",
        placeholder = "Enter Description",
        singleLine = false,
        modifier = Modifier.fillMaxWidth(0.9f)
    )
}