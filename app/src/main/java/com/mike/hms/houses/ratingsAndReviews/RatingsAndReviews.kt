package com.mike.hms.houses.ratingsAndReviews

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.HMSPreferences
import com.mike.hms.model.review.ReviewViewModel
import com.mike.hms.model.review.ReviewsWithUserInfo
import java.text.SimpleDateFormat
import java.util.Locale
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    reviewViewModel: ReviewViewModel = hiltViewModel()
) {

    val reviews by reviewViewModel.reviews.collectAsState()
    var selectedRating by remember { mutableStateOf<Int?>(null) }
    val filteredReviews = remember(selectedRating, reviews) {
        if (selectedRating != null) {
            reviews.filter { it.review.rating == selectedRating }
        } else {
            reviews
        }
    }

    LaunchedEffect(Unit) {
        reviewViewModel.getReviewsByUserId(HMSPreferences.userId.value)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Reviews & Ratings", style = CC.titleTextStyle())
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = "Back",
                            tint = CC.textColor()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CC.primaryColor()
                )
            )
        },
        containerColor = CC.primaryColor()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Rating Summary Card
            RatingSummaryCard(reviews)

            // Rating Filter Chips
            RatingFilterChips(
                selectedRating = selectedRating,
                onRatingSelected = { selectedRating = it }
            )

            // Reviews List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                if (filteredReviews.isEmpty()) {
                    item {
                        NoReviewsYetColumn(modifier.fillMaxSize())
                    }
                } else {
                    items(filteredReviews.size) { index ->
                        ReviewCard(filteredReviews[index])
                    }
                }
            }
        }
    }
}

@Composable
fun NoReviewsYetColumn(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "\uD83D\uDE4F", // Emoji: Pensive Face
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No reviews yet",
            style = CC.contentTextStyle()
        )
    }
}

@Composable
private fun RatingSummaryCard(reviews: List<ReviewsWithUserInfo>) {
    val averageRating = reviews.takeIf { it.isNotEmpty() }?.let {
        it.map { review -> review.review.rating }.average()
    } ?: 0.0

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
private fun RatingFilterChips(
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
private fun ReviewCard(reviewWithUser: ReviewsWithUserInfo) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = CC.extraSecondaryColor()
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Info and Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // User Avatar
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = CC.secondaryColor()
                    ) {
                        if (reviewWithUser.user.photoUrl.isNotEmpty()) {
                            AsyncImage(
                                model = reviewWithUser.user.photoUrl,
                                contentDescription = "User Avatar",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = reviewWithUser.user.firstName.first().toString(),
                                modifier = Modifier.fillMaxSize(),
                                textAlign = TextAlign.Center,
                                style = CC.titleTextStyle().copy(fontSize = 20.sp),
                                color = CC.primaryColor()
                            )
                        }
                    }

                    // User Name and Rating
                    Column {
                        Text(
                            text = reviewWithUser.user.firstName,
                            style = CC.contentTextStyle().copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                        RatingBar(
                            rating = reviewWithUser.review.rating.toFloat(),
                            size = 16.dp
                        )
                    }
                }

                // Expand Button
                IconButton(
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Show less" else "Show more",
                        modifier = Modifier.rotate(rotationState)
                    )
                }
            }

            // Review Preview (always visible)
            Text(
                text = reviewWithUser.review.reviewText,
                style = CC.contentTextStyle(),
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Expanded Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = CC.secondaryColor().copy(alpha = 0.2f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Posted on ${formatDate(reviewWithUser.review.timestamp)}",
                            style = CC.contentTextStyle().copy(
                                fontSize = 12.sp,
                                color = CC.textColor().copy(alpha = 0.7f)
                            )
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    // Copy review text to clipboard

                                    clipboardManager.setText(AnnotatedString(reviewWithUser.review.reviewText))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy review",
                                    tint = CC.secondaryColor()
                                )
                            }

                            IconButton(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT, """
                                            Review by ${reviewWithUser.user.firstName}
                                            Rating: ${reviewWithUser.review.rating}/5
                                            "${reviewWithUser.review.reviewText}"
                                        """.trimIndent()
                                        )
                                        type = "text/plain"
                                    }

                                    context.startActivity(
                                        Intent.createChooser(
                                            sendIntent,
                                            "Share Review"
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share review",
                                    tint = CC.secondaryColor()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingBar(
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
                    isHalfFilled -> Icons.Filled.StarHalf
                    else -> Icons.Filled.StarOutline
                },
                contentDescription = null,
                modifier = Modifier.size(size),
                tint = Color(0xFFFFD700) // Gold color for stars
            )
        }
    }
}

private fun formatDate(timestamp: String): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = formatter.parse(timestamp)
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        timestamp
    }
}