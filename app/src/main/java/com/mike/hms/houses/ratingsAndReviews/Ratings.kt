package com.mike.hms.houses.ratingsAndReviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.model.review.ReviewsWithUserInfo
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
 fun RatingSummaryCard(reviews: List<ReviewsWithUserInfo>) {
    val averageRating = reviews.takeIf { it.isNotEmpty() }?.map { review -> review.review.rating }
        ?.average()
        ?: 0.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CC.extraSecondaryColor()
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                String.format("%.1f", averageRating),
                style = CC.titleTextStyle().copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBar(rating = averageRating.toFloat(), size = 24.dp)
            }
            Text(
                "${reviews.size} ${if (reviews.size == 1) "Review" else "Reviews"}",
                style = CC.contentTextStyle().copy(
                    color = CC.textColor().copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
 fun RatingFilterChips(
    selectedRating: Int?,
    onRatingSelected: (Int?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedRating == null,
                onClick = { onRatingSelected(null) },
                label = {
                    Text(
                        "All",
                        style = CC.contentTextStyle()
                            .copy(color = if (selectedRating == null) CC.primaryColor() else CC.textColor())
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CC.secondaryColor(),
                )
            )
        }
        items(5) { rating ->
            FilterChip(
                selected = selectedRating == rating + 1,
                onClick = { onRatingSelected(if (selectedRating == rating + 1) null else rating + 1) },
                label = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = if (selectedRating == rating + 1) CC.primaryColor() else CC.textColor(),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "${rating + 1}",
                            style = CC.contentTextStyle()
                                .copy(color = if (selectedRating == rating + 1) CC.primaryColor() else CC.textColor())
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CC.secondaryColor(),
                    selectedLabelColor = CC.textColor()
                )
            )
        }
    }
}



@Composable
fun RatingBar(
    rating: Float,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(5) { index ->
            val isFilled = index < rating
            val isHalfFilled = index == rating.toInt() && rating % 1 != 0f

            Icon(
                imageVector = when {
                    isFilled -> Icons.Filled.Star
                    isHalfFilled -> Icons.AutoMirrored.Default.StarHalf
                    else -> Icons.Filled.StarOutline
                },
                contentDescription = null,
                modifier = Modifier.size(size),
                tint = Color(0xFFFFD700) // Gold color for stars
            )
        }
    }
}