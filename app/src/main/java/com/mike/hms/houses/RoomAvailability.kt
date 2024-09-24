package com.mike.hms.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.House
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun RoomsAndAvailability(house: HouseEntity) {
    val rooms = house.rooms.ifEmpty { listOf("No rooms available") }
    val roomNumber = if (rooms.size == 1) "Room" else "Rooms"
    val roomAvailability = if (house.houseAvailable) "Available" else "Unavailable"
    val availabilityColor = if (house.houseAvailable) Color.Green else Color.Red

    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rooms information
        Row (
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = CC.primaryColor(),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                Icons.Default.House,
                contentDescription = "Bed",
                tint = CC.secondaryColor(),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "${rooms.size} $roomNumber",
                style = CC.contentTextStyle().copy(color = CC.textColor())
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Availability information
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = availabilityColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(availabilityColor, RoundedCornerShape(8.dp))
                .padding(5.dp)
        ) {
            Text(
                text = roomAvailability,
                style = CC.contentTextStyle().copy(color = if (house.houseAvailable) CC.primaryColor() else CC.tertiaryColor())
            )
        }
    }
}
