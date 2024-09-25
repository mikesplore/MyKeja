package com.mike.hms.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun HouseGallery() {
    val house = houseTypes[0]
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Add padding to the entire column
        verticalArrangement = Arrangement.spacedBy(16.dp) // Space between each image card
    ) {
        items(house.houseImageLink) { imageUrl ->
            ImageCard(imageUrl = imageUrl)
        }
    }
}

@Composable
fun ImageCard(imageUrl: String) {
    Card(
        shape = RoundedCornerShape(12.dp), // Rounded corners for the card
        elevation = CardDefaults.cardElevation(8.dp), // Card elevation for shadow effect
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f) // Set a fixed aspect ratio to maintain consistent image sizes
    ) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = "House Image",
                contentScale = ContentScale.Crop, // Crop the image to fill the card
                modifier = Modifier.fillMaxSize()
            )

            // Optional overlay for image details or decoration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.4f)) // Add a translucent overlay at the bottom
                    .padding(8.dp) // Add padding to the overlay content
            ) {
                Text(
                    text = "Beautiful House Photo",
                    color = Color.White,
                    style = CC.contentTextStyle().copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
