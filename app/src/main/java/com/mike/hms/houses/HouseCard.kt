package com.mike.hms.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents

@Composable
fun HouseCard(house: HouseEntity, onHouseClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardHeight = screenWidth * 0.5f
    val cardWidth = screenWidth * 0.49f

    val brush = Brush.horizontalGradient(
        colors = listOf(
            CommonComponents.primaryColor(),
            CommonComponents.tertiaryColor().copy(alpha = 0.5f)
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
        val link  = if(house.houseImageLink.isNotEmpty()) house.houseImageLink[0] else ""
        Column {
            AsyncImage(
                model = link,
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
                    style = CommonComponents.titleTextStyle()
                        .copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    house.houseLocation,
                    style = CommonComponents.contentTextStyle().copy(color = CommonComponents.extraPrimaryColor())
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "★ ${house.houseRating}",
                        style = CommonComponents.contentTextStyle().copy(
                            color = Color(0xFFFFA000)
                        )
                    )
                    Text(
                        house.numberOfRooms.toString() + if (house.numberOfRooms == 1) " room" else " rooms",
                        style = CommonComponents.contentTextStyle().copy(fontSize = 13.sp)
                    )
                }
            }
        }
    }
}