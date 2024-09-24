package com.mike.hms.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.rooms
import com.mike.hms.model.roomModel.RoomCategory
import com.mike.hms.model.roomModel.RoomEntity
import java.util.Locale
import com.mike.hms.ui.theme.CommonComponents as CC


val roomCategory = listOf(
    RoomCategory.ECONOMY,
    RoomCategory.STANDARD,
    RoomCategory.FAMILY_SUITE,
    RoomCategory.LUXURY,
    RoomCategory.DELUXE,
    RoomCategory.EXECUTIVE,
)

object FilteredCategory {
    val category: MutableState<String> = mutableStateOf("")
}

@Composable
fun RoomCategoryBox(roomCategory: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) CC.extraPrimaryColor() else CC.surfaceContainerColor(),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = roomCategory,
            style = CC.contentTextStyle().copy(
                color = if (isSelected) CC.primaryColor() else CC.secondaryColor()
            ),
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun RoomsCategory() {
    var selectedIndex by remember { mutableIntStateOf(-1) } // Track the selected index

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(roomCategory.size) { index ->
            val formattedCategory = roomCategory[index]
                .toString()
                .replace("_", " ") // Replace underscore with space
                .lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

            RoomCategoryBox(
                roomCategory = formattedCategory,
                isSelected = selectedIndex == index, // Check if this box is selected
                onClick = {
                    FilteredCategory.category.value = formattedCategory
                    selectedIndex = index
                } // Update selected index when clicked
            )
        }
    }
}


@Composable
fun RoomCategoryItem(room: RoomEntity, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val boxWidth = screenWidth * 0.35f
    val boxHeight = boxWidth * 1.3f
    val density = LocalDensity.current
    val textSize = with(density) { (boxHeight * 0.1f).toSp() }

    // Card for the room item
    Card(
        modifier = modifier
            .width(boxWidth)
            .height(boxHeight),
        shape = RoundedCornerShape(20.dp),  // Rounded corners
        elevation = CardDefaults.cardElevation(5.dp)  // Built-in shadow/elevation
    ) {
        Box {
            // Room image
            AsyncImage(
                model = room.roomImageLink.firstOrNull(),
                contentDescription = "Room Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            // Display the room price at the bottom end
            Box(
                modifier = Modifier
                    .background(CC.secondaryColor(), shape = RoundedCornerShape(10.dp))
                    .align(Alignment.TopEnd)  // Align to the bottom end
            ) {
                Text(
                    text = String.format(Locale.getDefault(), "Ksh %,d/Night", room.roomPrice),
                    style = CC.bodyTextStyle().copy(
                        fontSize = textSize * 0.7f,
                        color = CC.primaryColor()
                    ),
                    modifier = Modifier
                        .padding(5.dp)
                )
            }

            // Bottom overlay for room details (type, capacity, rating)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)  // Align the other details to the bottom start
                    .fillMaxWidth()
                    .background(
                        CC
                            .surfaceContainerColor()
                            .copy(alpha = 0.9f)
                    )
                    .padding(5.dp)  // Padding for a cleaner layout
            ) {
                // Room type and capacity
                Text(
                    text = room.roomType.name.first()
                        .uppercase(Locale.getDefault()) + room.roomType.name.substring(1)
                        .lowercase(Locale.getDefault()),
                    style = CC.bodyTextStyle().copy(
                        fontSize = textSize * 0.7f,
                        color = CC.extraPrimaryColor()
                    ),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
                val guests= if (room.roomCapacity == 1) " ${room.roomCapacity} Guest" else " ${room.roomCapacity} Guests"
                Text(
                    text = guests,
                    style = CC.bodyTextStyle().copy(
                        fontSize = textSize * 0.7f,
                        color = CC.extraPrimaryColor()
                        ),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }
    }
}


@Composable
fun RecommendedRoomTypeList(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .padding(start = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        val filteredRooms = if (FilteredCategory.category.value.isEmpty()) {
            rooms // Don't filter if category is empty
        } else {
            rooms.filter { room ->
                room.roomCategory
                    .toString()
                    .replace("_", " ")
                    .lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } == FilteredCategory.category.value
            }
        }
        items(filteredRooms) { houseCategory ->
            RoomCategoryItem(houseCategory)
        }
    }
}


