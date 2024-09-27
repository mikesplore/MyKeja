package com.mike.hms.houses.bookHouse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mike.hms.houses.formatNumber
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun HouseDetailsCard(house: HouseEntity) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val brush = Brush.horizontalGradient(
        listOf(CC.primaryColor(), CC.secondaryColor().copy(alpha = 0.5f), CC.primaryColor())
    )
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .height(screenHeight * 0.11f)
            .fillMaxWidth(0.9f)
    ) {
        Column(
            modifier = Modifier

                .background(CC.primaryColor())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    house.houseName,
                    style = CC.titleTextStyle(),
                    fontWeight = FontWeight.Bold,
                    fontSize = CC.titleTextStyle().fontSize * 1.2f,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Ksh. ${formatNumber(house.housePrice)} /Night",
                    style = CC.bodyTextStyle(),
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Icon",
                        tint = CC.secondaryColor()
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(house.houseLocation, style = CC.bodyTextStyle())
                }

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Location Icon",
                        tint = CC.secondaryColor()
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(house.houseRating, style = CC.bodyTextStyle())
                }

            }
        }
    }
}






@Composable
fun TotalPriceRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                CC
                    .secondaryColor()
                    .copy(alpha = 0.1f)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total Price",
            style = CC.bodyTextStyle().copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Ksh 45,000",
            style = CC.titleTextStyle().copy(
                fontWeight = FontWeight.ExtraBold,
                color = CC.tertiaryColor()
            )
        )
    }
}
