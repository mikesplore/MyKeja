package com.mike.hms.dashboard

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
import java.util.Locale
import com.mike.hms.ui.theme.CommonComponents as CC


@Composable
fun PopularHouseItem(houseType: HouseEntity, modifier: Modifier = Modifier, navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val boxWidth = screenWidth * 0.35f
    val boxHeight = boxWidth * 1.3f
    val density = LocalDensity.current
    val textSize = with(density) { (boxHeight * 0.1f).toSp() }

    // Card for the house item
    Card(
        modifier = modifier
            .clickable{
                navController.navigate("houseDetails/${houseType.houseID}")
            }
            .width(boxWidth)
            .height(boxHeight),
        shape = RoundedCornerShape(20.dp),  // Rounded corners
        elevation = CardDefaults.cardElevation(5.dp)                 // Built-in shadow/elevation
    ) {
        Box {
            // Image
            AsyncImage(
                model = houseType.houseImageLink.firstOrNull(),
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
                    .background(CC.secondaryColor())
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
                        color = CC.primaryColor()
                    ),
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }
            Row(
                modifier = Modifier
                    .background(CC.secondaryColor(), RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                    .align(Alignment.TopStart)
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color.Yellow,
                    modifier = Modifier.size(18.dp),
                    )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = houseType.houseRating.toString(),
                    style = CC.bodyTextStyle().copy(
                        fontSize = textSize * 0.6f,
                        color = CC.primaryColor()
                        ),
                )
            }
        }
    }

}


@Composable
fun PopularHouseTypeList(
    modifier: Modifier = Modifier,
    houseViewModel: HouseViewModel,
    houses: List<HouseEntity> = emptyList(),
    navController: NavController
) {
    val housesLoading by houseViewModel.isHouseLoading.collectAsState()
    val sortedHouses = houses.sortedByDescending { it.houseRating } // Sort by rating in descending order

    LazyRow(
    modifier = modifier
        .animateContentSize()
        .padding(start = 20.dp)
        .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(15.dp)
) {
    items(if (housesLoading) 1 else sortedHouses.size) { index ->
        if (housesLoading) {
            CircularProgressIndicator()
        } else {
            PopularHouseItem(sortedHouses[index], navController = navController)
        }
    }
}
}
