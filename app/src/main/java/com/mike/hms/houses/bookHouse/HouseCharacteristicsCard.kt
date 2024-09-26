package com.mike.hms.houses.bookHouse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents

@Composable
fun HouseCharacteristicsCard(house: HouseEntity) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = CommonComponents.primaryColor()),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier

                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CharacteristicsColumn(
                    title1 = "Check-In",
                    value1 = "01/02/2024",
                    title2 = "Check-Out",
                    value2 = "11/02/2024"
                )
                CharacteristicsColumn(
                    title1 = "House Type",
                    value1 = house.houseType.toString(),
                    title2 = "Capacity",
                    value2 = "${house.rooms.size} Rooms"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TotalPriceRow()
        }
    }
}


@Composable
fun CharacteristicsColumn(
    title1: String,
    value1: String,
    title2: String,
    value2: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CharacteristicItem(title = title1, value = value1)
        CharacteristicItem(title = title2, value = value2)
    }
}

@Composable
fun CharacteristicItem(title: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(CommonComponents.extraPrimaryColor())
        )
        Column {
            Text(
                text = title,
                style = CommonComponents.bodyTextStyle().copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = value,
                style = CommonComponents.bodyTextStyle().copy(
                    color = CommonComponents.extraPrimaryColor(),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}