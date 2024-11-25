package com.mike.hms.houses.ratingsAndReviews

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mike.hms.HMSPreferences
import com.mike.hms.model.review.ReviewViewModel
import com.mike.hms.model.review.ReviewsWithUserInfo
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun ReviewCard(
    reviewWithUser: ReviewsWithUserInfo,
    reviewViewModel: ReviewViewModel = hiltViewModel()
) {
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
                            text = "Posted on ${CC.formatDateToShortDate(reviewWithUser.review.timestamp)}",
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

                            //delete button
                            if (reviewWithUser.review.userId == HMSPreferences.userId.value) {
                                IconButton(
                                    onClick = {
                                        //delete review
                                        reviewViewModel.deleteReview(reviewWithUser.review.id) { success ->
                                            if (success) {
                                                Toast.makeText(
                                                    context,
                                                    "Review deleted",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
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
}