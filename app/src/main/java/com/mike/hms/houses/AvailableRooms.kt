package com.mike.hms.houses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.rooms
import com.mike.hms.model.roomModel.RoomEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun AvailableRooms() {
    val rooms = rooms
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, bottom = 10.dp),
    ) {
        Text(
            text = "Step Inside",
            style = CC.titleTextStyle().copy(color = CC.tertiaryColor())
        )

    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(rooms.size) { index ->
            val room = rooms[index]
            RoomItem(room)
        }

    }
}


@Composable
fun RoomItem(room: RoomEntity, modifier: Modifier = Modifier) {
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
        }
    }
}