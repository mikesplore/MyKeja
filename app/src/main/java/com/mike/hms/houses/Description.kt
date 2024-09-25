package com.mike.hms.houses

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun HouseDescription(house: HouseEntity) {
    val brush = Brush.horizontalGradient(
        listOf(CC.primaryColor(), CC.extraSecondaryColor().copy(alpha = 0.5f))
    )

    // Track the expanded state
    var isExpanded by remember { mutableStateOf(false) }

    // Split the description into words and limit it to 25
    val words = house.houseDescription.split(" ")
    val displayedText = if (isExpanded) {
        house.houseDescription
    } else {
        words.take(25).joinToString(" ") + if (words.size > 25) "..." else ""
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = CC.surfaceContainerColor()
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush, shape = RoundedCornerShape(10.dp))
                .animateContentSize() // Animate size changes
        ) {
            Text(
                text = "Description",
                style = CC.titleTextStyle().copy(
                    fontSize = 18.sp,
                    color = CC.tertiaryColor()
                ),
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = displayedText,
                style = CC.contentTextStyle().copy(
                    color = CC.textColor(),
                    lineHeight = 20.sp
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            // Show "Read More" or "Read Less" based on the expanded state
            if (words.size > 25) {
                Text(
                    text = if (isExpanded) "Read Less" else "Read More",
                    style = CC.bodyTextStyle().copy(color = CC.extraPrimaryColor()),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                        .clickable { isExpanded = !isExpanded } // Toggle the expanded state
                )
            }
        }
    }
}

