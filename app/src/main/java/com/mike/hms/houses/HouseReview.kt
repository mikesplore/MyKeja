package com.mike.hms.houses

import android.content.Context
import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.HMSPreferences
import com.mike.hms.authentication.GoogleAuth
import com.mike.hms.houses.ratingsAndReviews.NoReviewsYetColumn
import com.mike.hms.model.review.ReviewEntity
import com.mike.hms.model.review.ReviewViewModel
import com.mike.hms.model.review.ReviewsWithUserInfo
import kotlinx.coroutines.launch
import java.text.NumberFormat
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseReviewsScreen(houseId: String, navController: NavController, context: Context) {
    val reviewViewModel: ReviewViewModel = hiltViewModel()
    val reviews by reviewViewModel.reviews.collectAsState()
    val averageRating = reviews.map { it.review.rating }.average()
    val totalReviews = reviews.size

    var showBottomSheet by remember { mutableStateOf(false) }
    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        reviewViewModel.getReviewsByHouseId(houseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "House Reviews",
                        style = CC.titleTextStyle().copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Back",
                            tint = CC.textColor()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CC.extraSecondaryColor()
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = CC.secondaryColor()
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Review",
                    tint = CC.primaryColor()
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            RatingStatistics(averageRating, totalReviews)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (reviews.isEmpty()) {
                    item {
                        NoReviewsYetColumn(modifier = Modifier.fillMaxSize())
                    }
                } else {
                    items(reviews) { review ->
                        ReviewItem(review)
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    rating = 0
                    reviewText = ""
                },
                sheetState = rememberModalBottomSheetState(),
                containerColor = CC.primaryColor()
            ) {
                if (FirebaseAuth.getInstance().currentUser == null){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "You need to Sign in to add reviews", style = CC.contentTextStyle())
                        Spacer(modifier = Modifier.height(16.dp))

                            GoogleAuth { onSuccess ->
                                if (onSuccess){
                                    showBottomSheet = false
                                    showBottomSheet = true
                                }
                            }

                    }
                }
                else{
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Add Review",
                            style = MaterialTheme.typography.titleLarge,
                            color = CC.textColor()
                        )

                        // Rating selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(5) { index ->
                                IconButton(
                                    onClick = { rating = index + 1 }
                                ) {
                                    Icon(
                                        imageVector = if (index < rating) {
                                            Icons.Filled.Star
                                        } else {
                                            Icons.Outlined.Star
                                        },
                                        contentDescription = "Star ${index + 1}",
                                        tint = if (index < rating) {
                                            Color(0xFFFFD700)
                                        } else {
                                            CC.textColor()
                                        }
                                    )
                                }
                            }
                        }

                        // Review text field
                        OutlinedTextField(
                            value = reviewText,
                            onValueChange = { reviewText = it },
                            label = { Text("Write your review") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            colors = CC.outLinedTextFieldColors(),
                            shape = CC.outLinedTextFieldShape(),
                            textStyle = CC.contentTextStyle()
                        )

                        // Submit button
                        Button(
                            onClick = {
                                if (rating == 0 || reviewText.isBlank() || HMSPreferences.userId.value.isBlank() || houseId.isEmpty()){
                                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                CC.generateReviewId { id ->
                                    val review = ReviewEntity(
                                        id = id,
                                        userId = HMSPreferences.userId.value,
                                        houseId = houseId,
                                        rating = rating,
                                        reviewText = reviewText,
                                        timestamp = CC.getCurrentDate()
                                    )
                                    reviewViewModel.insertReview(review){success ->
                                        if (success){
                                            reviewViewModel.getReviewsByHouseId(houseId)
                                            showBottomSheet = false
                                            rating = 0
                                            reviewText = ""
                                        }
                                        else {
                                            Toast.makeText(context, "Failed to add review", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CC.buttonColors(),
                            enabled = rating > 0 && reviewText.isNotBlank()
                        ) {
                            Text("Submit Review", style = CC.contentTextStyle())
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}




@Composable
fun RatingStatistics(averageRating: Double, totalReviews: Int) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val brush = Brush.linearGradient(
        colors = listOf(
            CC.primaryColor(),
            CC.secondaryColor()
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(screenHeight * 0.15f),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingCircle(averageRating)
                Spacer(modifier = Modifier.width(16.dp))
                RatingDetails(averageRating, totalReviews)
            }
        }
    }
}

@Composable
fun RatingCircle(averageRating: Double) {
    val formattedRating = remember(averageRating) {
        NumberFormat.getNumberInstance().apply {
            maximumFractionDigits = 1
        }.format(averageRating)
    }

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(CC.extraPrimaryColor()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = formattedRating,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = CC.primaryColor()
            )
        )
    }
}

@Composable
fun RatingDetails(averageRating: Double, totalReviews: Int) {
    Column {
        Text(
            text = "Average Rating",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < averageRating.toInt()) Color.Yellow else Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "($totalReviews reviews)",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Based on feedback from our valued guests",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f))
        )
    }
}


@Composable
fun ReviewItem(review: ReviewsWithUserInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Reviewer avatar (placeholder)
        Box(
            modifier = Modifier
                .background(
                    color = CC.extraSecondaryColor(),
                    shape = CircleShape
                )
                .size(40.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center


        ) {
            // You can replace this with an actual avatar image if available
            Text(
                text = review.user.firstName.first().toString(),
                modifier = Modifier.padding(8.dp),
                color = CC.textColor()
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = review.user.firstName,
                style = CC.contentTextStyle().copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Rating: ${review.review.rating} ⭐",
                style = CC.contentTextStyle()
            )
            Text(
                text = review.review.reviewText,
                style = CC.contentTextStyle(),
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = review.review.timestamp,
                style = CC.contentTextStyle().copy(color = Color.Gray)
            )
        }
    }
}