package com.mike.hms.dashboard

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.houses.formatNumber
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
import java.util.Locale
import com.mike.hms.ui.theme.CommonComponents as CC


@Composable
fun PopularHouseItem(
    houseType: HouseEntity,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = screenWidth * 0.8f
    val cardHeight = cardWidth * 0.5f

    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                navController.navigate("houseDetails/${houseType.houseID}")
            }
            .width(cardWidth)
            .height(cardHeight),
        shape = RoundedCornerShape(12.dp), // Subtle rounded corners
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            // Background Image
            AsyncImage(
                model = houseType.houseImageLink.firstOrNull(),
                contentDescription = "House Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay for better text visibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // House details overlay
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Image Thumbnail
                Box(
                    modifier = Modifier
                        .size(cardHeight * 0.7f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CC.secondaryColor()),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = houseType.houseImageLink.firstOrNull(),
                        contentDescription = "Thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Text Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = houseType.houseName,
                        style = CC.titleTextStyle().copy(
                            fontSize = 18.sp,
                            color = Color.White
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${houseType.houseLocation} - ${
                            houseType.houseType.name.capitalize(Locale.getDefault())
                        }",
                        style = CC.bodyTextStyle().copy(
                            fontSize = 14.sp,
                            color = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${houseType.houseRating}/5",
                            style = CC.bodyTextStyle().copy(
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            // Price Tag
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(bottomStart = 12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Ksh ${formatNumber(houseType.housePrice)}",
                    style = CC.bodyTextStyle().copy(
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
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
    val sortedHouses =
        houses.sortedByDescending { it.houseRating } // Sort by rating in descending order

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
