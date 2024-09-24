package com.mike.hms.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HouseAmenities(house: HouseEntity) {
    val amenities = house.houseAmenities.map { it.name }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val brush = Brush.horizontalGradient(
        listOf(CC.primaryColor(), CC.secondaryColor().copy(alpha = 0.5f))
    )

    Column(
        modifier = Modifier
            .background(
                brush, RoundedCornerShape(10.dp)
            )
            .padding(bottom = 10.dp)
            .fillMaxWidth(0.9f)
            .heightIn(max = screenHeight * 0.2f)
    ) {
        Text(
            "Amenities", style = CC.titleTextStyle().copy(
                color = CC.tertiaryColor()
            ),
            fontWeight = FontWeight.Bold,
            modifier =
            Modifier.padding(16.dp)
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)


        ) {
            amenities.forEach { amenity ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = CC.primaryColor(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = amenity.first().uppercase() + amenity.substring(1).lowercase().replace("_", " "),
                        style = CC.contentTextStyle().copy(
                            fontSize = 14.sp,
                            color = CC.textColor(),
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}