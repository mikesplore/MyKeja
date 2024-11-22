package com.mike.hms.profile.paymentMethods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mike.hms.model.paymentMethods.CreditCardWithUser

@Composable
fun CreditCard(
    creditCardWithUser: CreditCardWithUser,
    onDelete: () -> Unit,

    ) {
    val cardImageUrl = listOf(
        "https://img.freepik.com/free-photo/view-wild-lion-nature_23-2150460851.jpg",
        "https://media.wired.com/photos/65caa6f6f553745750c04769/master/w_2560%2Cc_limit/elephant-congo-science-GettyImages-630005418.jpg",
        "https://files.worldwildlife.org/wwfcmsprod/images/White_Rhino/hero_small/3yuabfu3jq_white_rhino_42993643.jpg",
        "https://d1jyxxz9imt9yb.cloudfront.net/animal/234/meta_image/regular/LC202303_AmboseliCommsSummit_082_404092_reduced.jpg"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298))
                    )
                )
        ) {
            // Delete Button
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Card",
                    tint = Color.White
                )
            }

            // Card Image or Placeholder
            AsyncImage(
                model = cardImageUrl.random(),
                contentDescription = "Card Image",
                modifier = Modifier
                    .fillMaxSize()  // Make the image cover the entire card
                    .align(Alignment.Center),  // Center the image
                contentScale = ContentScale.Crop  // Crop the image to cover the card area
            )

            // Overlay with text and details
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Add padding to ensure text doesn't overlap the edges
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Card Issuer Logo/Text
                Text(
                    text = "VISA",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.align(Alignment.End)
                )

                // Card Number
                Text(
                    text = creditCardWithUser.creditCard.cardNumber
                        .chunked(4)
                        .joinToString(" "),
                    fontSize = 22.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    style = TextStyle( // Apply bold and shadow
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Black, // Shadow color
                            offset = Offset(2f, 2f), // Shadow offset
                            blurRadius = 4f // Shadow blur radius
                        )
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Card Holder Name
                    Column {
                        Text(
                            text = "Card Holder",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${creditCardWithUser.user.firstName} ${creditCardWithUser.user.lastName}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Expiry Date
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Expires",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = creditCardWithUser.creditCard.expiryDate,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}