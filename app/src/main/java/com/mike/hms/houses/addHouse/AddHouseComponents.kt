package com.mike.hms.houses.addHouse

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.mike.hms.model.houseModel.HouseAmenities
import com.mike.hms.model.houseModel.HouseCategory
import com.mike.hms.model.houseModel.HouseType
import com.mike.hms.ui.theme.CommonComponents as CC

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

) {
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
            onValueChange = { onHouseRatingChange(it) },
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

) {
    CC.MyOutlinedTextField(
        value = houseDescription,
        onValueChange = { onHouseDescriptionChange(it) },
        label = "Description",
        placeholder = "Enter Description",
        singleLine = false,
        modifier = Modifier.fillMaxWidth(0.9f)
    )
}

@Composable
fun HouseImages(
    modifier: Modifier = Modifier,
    imageLinks: List<String>,
    onImageLinksChange: (List<String>) -> Unit,
    currentImageLink: String,
    onCurrentImageLinkChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
    Text(
        "Image Links",
        style = CC.titleTextStyle().copy(color = CC.tertiaryColor()),
        modifier = modifier
    )}

    // Input field and add button
    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CC.MyOutlinedTextField(
            value = currentImageLink,
            onValueChange = onCurrentImageLinkChange, // Pass the callback
            placeholder = "Enter Image Link",
            singleLine = true,
            label = "Image Link",
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                if (currentImageLink.isNotEmpty()) {
                    onImageLinksChange(imageLinks + currentImageLink) // Update imageLinks
                    onCurrentImageLinkChange("") // Clear currentImageLink
                }
            }
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Image", tint = Color.Green)
        }
    }

    // Display image previews with delete option in a LazyRow
    LazyRow(
        modifier = Modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(imageLinks) { imageLink ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(8.dp)
                    .size(100.dp) // Thumbnail size
                    .border(1.dp, CC.extraSecondaryColor(), RoundedCornerShape(10.dp)) // Border around the image
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageLink),
                    contentDescription = "Image Preview",
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop // Crop the image to fill the box
                )
                IconButton(
                    onClick = { onImageLinksChange(imageLinks - imageLink) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Image", tint = Color.Red)
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HouseTypeSelection(
    selectedHouseType: HouseType?,
    onHouseTypeSelected: (HouseType?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Text(
            "House Type",
            style = CC.titleTextStyle().copy(color = CC.tertiaryColor()),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
    FlowRow(
        modifier = Modifier.fillMaxWidth(0.9f),
    ) {
        HouseType.entries.forEach { houseType ->
            FilterChip(
                modifier = Modifier
                    .padding(start = 8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CC.extraSecondaryColor(),
                    selectedLabelColor = CC.extraPrimaryColor(),
                    labelColor = CC.textColor()

                ),
                selected = houseType == selectedHouseType,
                onClick = {
                    onHouseTypeSelected(if (houseType == selectedHouseType) null else houseType)
                },
                label = { Text(CC.transformText(houseType.name), style = CC.bodyTextStyle()) }
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HouseCategorySelection(
    selectedHouseCategory: HouseCategory?,
    onHouseCategorySelected: (HouseCategory?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "House Category",
            style = CC.titleTextStyle().copy(color = CC.tertiaryColor()),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
    FlowRow(
        modifier = Modifier.fillMaxWidth(0.9f),
    ) {
        HouseCategory.entries.forEach { category ->
            FilterChip(
                modifier = Modifier
                    .padding(start = 8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CC.extraSecondaryColor(),
                    selectedLabelColor = CC.extraPrimaryColor(),
                    labelColor = CC.textColor()

                ),
                selected = category == selectedHouseCategory,
                onClick = {
                    onHouseCategorySelected(if (category == selectedHouseCategory) null else category)
                },
                label = { Text(CC.transformText(category.name), style = CC.bodyTextStyle()) }
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HouseAmenitiesSelection(
    selectedHouseAmenities: List<HouseAmenities>,
    onHouseAmenitySelected: (HouseAmenities) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Text(
            "House Amenities",
            style = CC.titleTextStyle().copy(color = CC.tertiaryColor()),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
    FlowRow(
        modifier = Modifier.fillMaxWidth(0.9f),
    ) {
        HouseAmenities.entries.forEach { amenity ->
            FilterChip(
                modifier = Modifier
                    .padding(start = 8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CC.extraSecondaryColor(),
                    selectedLabelColor = CC.extraPrimaryColor(),
                    labelColor = CC.textColor()

                ),
                selected = selectedHouseAmenities.contains(amenity),
                onClick = {
                    onHouseAmenitySelected(amenity)
                },
                label = { Text(CC.transformText(amenity.name), style = CC.bodyTextStyle()) }
            )
        }
    }
}
