package com.mike.hms.houses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC

/**
 * Composable function to display house location and ratings.
 * @param house The house entity containing location and ratings.
 * @param ratingCount The count of ratings.
 * @param ratingAverage The average rating.
 * @param navController The NavController for navigation.
 *
 */
@Composable
fun HouseLocationAndRatings(house: HouseEntity, ratingCount: String, ratingAverage: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Location Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f) // Adjust weight for better layout balance
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = CC.secondaryColor(),
                modifier = Modifier.size(20.dp) // Adjust size of the icon for a cleaner look
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = house.houseLocation,
                style = CC.titleTextStyle().copy(
                    fontSize = 14.sp,
                    color = CC.tertiaryColor()
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // Ratings Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .clickable{navController.navigate("houseReviews/${house.houseID}")}
                .weight(1f) // Balance weight with location row
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFFC107), // Custom yellow tint for star rating
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (ratingAverage.isNotEmpty()) ratingAverage else "",
                style = CC.titleTextStyle().copy(
                    fontSize = 14.sp,
                    color = CC.textColor(),
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (ratingCount == 0.toString()) "(No Reviews)" else if (ratingCount == "1") "($ratingCount Review)" else "($ratingCount Reviews)",
                style = CC.bodyTextStyle().copy(
                    fontSize = 12.sp,
                    color = CC.secondaryColor()
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = CC.secondaryColor()
            )
        }
    }
}