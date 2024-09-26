package com.mike.hms.houses.bookHouse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.homeScreen.User
import com.mike.hms.ui.theme.CommonComponents

@Composable
fun UserDetails(user: User) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val brush = Brush.horizontalGradient(
        listOf(CommonComponents.primaryColor(), CommonComponents.secondaryColor().copy(alpha = 0.5f), CommonComponents.primaryColor())
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.14f) // Increased height for better spacing
            .padding(horizontal = 16.dp, vertical = 8.dp) // Added outer padding
    ) {
        Row(
            modifier = Modifier
                .background(brush)
                .fillMaxSize()
                .padding(16.dp) // Inner padding for content
        ) {
            // User avatar
            Box(
                modifier = Modifier
                    .background(CommonComponents.secondaryColor(), shape = CircleShape)
                    .clip(CircleShape)
                    .size(screenWidth * 0.15f), // Adjusted size for balance
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.fullName.first().toString(),
                    style = CommonComponents.titleTextStyle().copy(
                        fontSize = 24.sp, // Larger font for initials
                        fontWeight = FontWeight.Bold,
                        color = CommonComponents.primaryColor() // Text color for avatar
                    )
                )
            }

            Spacer(modifier = Modifier.width(16.dp)) // Spacer for better separation

            // User details
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.fullName,
                    style = CommonComponents.titleTextStyle().copy(
                        fontSize = 20.sp, // Increased font size for name
                        fontWeight = FontWeight.Bold,
                        color = CommonComponents.textColor()
                    )
                )
                Spacer(modifier = Modifier.height(4.dp)) // Small gap between text
                Text(
                    text = user.email,
                    style = CommonComponents.contentTextStyle().copy(
                        color = CommonComponents.textColor().copy(alpha = 0.8f) // Slightly lighter color
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .width(screenWidth * 0.4f)
                        .background(CommonComponents.extraSecondaryColor(), shape = RoundedCornerShape(10.dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Location Icon",
                        tint = CommonComponents.secondaryColor(),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Premium Member", // Membership status
                        style = CommonComponents.contentTextStyle().copy(
                            color = CommonComponents.secondaryColor(),
                            fontStyle = FontStyle.Italic,
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
