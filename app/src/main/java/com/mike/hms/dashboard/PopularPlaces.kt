package com.mike.hms.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC


@Composable
fun PopularHouseItem(houseType: HouseEntity, modifier: Modifier = Modifier) {
    BoxWithConstraints {
        val boxWidth = maxHeight * 0.4f
        val boxHeight = boxWidth * 1.3f
        val density = LocalDensity.current
        val textSize = with(density) { (boxHeight * 0.1f).toSp() }

        Box(
            modifier = modifier
                .width(boxWidth)
                .height(boxHeight)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    color = CC.secondaryColor(),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            // Image
            AsyncImage(
                model = houseType.houseImageLink,
                contentDescription = "House Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            // Bottom overlay with house name, location, and rating
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(CC.surfaceContainerColor().copy(alpha = 0.9f))
                    .padding(10.dp) // Add padding for a cleaner look
            ) {
                // House name and location
                Text(
                    text = "${houseType.houseName}, ${houseType.houseLocation}",
                    style = CC.bodyTextStyle().copy(
                        fontSize = textSize * 0.7f,
                        color = CC.extraPrimaryColor()
                    ),
                    modifier = Modifier.padding(bottom = 6.dp) // Space above the rating
                )

                // Row for rating with star icon
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Center the star and rating
                    modifier = Modifier.padding(start = 4.dp) // Add slight padding for structure
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = CC.extraPrimaryColor(),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp)) // Space between the star and rating text
                    Text(
                        text = houseType.houseRating,
                        style = CC.bodyTextStyle().copy(
                            fontSize = textSize * 0.7f,
                            color = CC.extraPrimaryColor()
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun PopularHouseTypeList(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .padding(start = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(houseTypes) { houseType ->
            PopularHouseItem(houseType)
        }
    }
}
