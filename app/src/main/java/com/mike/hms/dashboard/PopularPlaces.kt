package com.mike.hms.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.model.houseModel.HouseEntity
import java.util.Locale
import com.mike.hms.ui.theme.CommonComponents as CC


@Composable
fun PopularHouseItem(houseType: HouseEntity, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val boxWidth = screenWidth * 0.35f
    val boxHeight = boxWidth * 1.3f
    val density = LocalDensity.current
    val textSize = with(density) { (boxHeight * 0.1f).toSp() }

    // Card for the house item
    Card(
        modifier = modifier
            .width(boxWidth)
            .height(boxHeight),
        shape = RoundedCornerShape(20.dp),  // Rounded corners
        elevation = CardDefaults.cardElevation(5.dp)                 // Built-in shadow/elevation
    ) {
        Box {
            // Image
            AsyncImage(
                model = houseType.houseImageLink.random(),
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
                    .background(
                        CC
                            .surfaceContainerColor()
                            .copy(alpha = 0.9f)
                    )
                    .padding(5.dp)  // Padding for a cleaner layout
            ) {
                // House name, type and location
                Text(
                    text = "${houseType.houseName}, ${
                        if (houseType.houseType.name.startsWith(
                                "a",
                                ignoreCase = true
                            )
                        ) "an" else "a"
                    } ${
                        houseType.houseType.name.lowercase(
                            Locale.getDefault()
                        )
                    } in ${houseType.houseLocation}",
                    style = CC.bodyTextStyle().copy(
                        fontSize = textSize * 0.6f,
                        color = CC.tertiaryColor()
                    ),
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                // Row for rating with star icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,  // Center the star and rating
                    modifier = Modifier.padding(start = 4.dp)  // Padding for structure
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = CC.extraPrimaryColor(),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))  // Space between star and rating
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
