package com.mike.hms.houses

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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.homeScreen.mockUser
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingInfoScreen(
) {
    val house = houseTypes[0]
    val user = mockUser
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val brush = Brush.horizontalGradient(
        listOf(CC.primaryColor(), CC.secondaryColor())
    )

    // Scaffold with a top app bar titled "Ticket"
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ticket") },
                colors = CC.topAppBarColors()
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text("Order Details", style = CC.titleTextStyle(), fontWeight = FontWeight.Bold)
                }
                // User Card
                UserDetails(user)

                // House Details Card
                HouseDetailsCard(house)

                // House Characteristics Card
                HouseCharacteristicsCard(house)

                // Reschedule Card
                Card(
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Reschedule",
                            style = CC.titleTextStyle(),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Old Date: 2024-09-28", style = CC.contentTextStyle())
                        Text(text = "New Date: 2024-09-30", style = CC.contentTextStyle())
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Proceed to Checkout Button
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CC.secondaryColor()
                    )
                ) {
                    Text(text = "Proceed to Checkout")
                }
            }
        }
    )
}


@Composable
fun UserDetails(user: User) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val brush = Brush.horizontalGradient(
        listOf(CC.primaryColor(), CC.secondaryColor().copy(alpha = 0.5f), CC.primaryColor())
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
                    .background(CC.secondaryColor(), shape = CircleShape)
                    .clip(CircleShape)
                    .size(screenWidth * 0.15f), // Adjusted size for balance
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.fullName.first().toString(),
                    style = CC.titleTextStyle().copy(
                        fontSize = 24.sp, // Larger font for initials
                        fontWeight = FontWeight.Bold,
                        color = CC.primaryColor() // Text color for avatar
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
                    style = CC.titleTextStyle().copy(
                        fontSize = 20.sp, // Increased font size for name
                        fontWeight = FontWeight.Bold,
                        color = CC.textColor()
                    )
                )
                Spacer(modifier = Modifier.height(4.dp)) // Small gap between text
                Text(
                    text = user.email,
                    style = CC.contentTextStyle().copy(
                        color = CC.textColor().copy(alpha = 0.8f) // Slightly lighter color
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Premium Member", // Membership status
                    style = CC.contentTextStyle().copy(
                        color = CC.extraSecondaryColor(),
                        fontStyle = FontStyle.Italic // Italic for premium status
                    )
                )
            }
        }
    }
}


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
            .height(screenHeight * 0.13f)
            .fillMaxWidth(0.9f)
    ) {
        Column(
            modifier = Modifier
                .background(brush)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
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
                    "Ksh. ${house.housePrice} /Night",
                    style = CC.bodyTextStyle(),
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(16.dp)
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
fun HouseCharacteristicsCard(house: HouseEntity) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "House Characteristics",
                style = CC.titleTextStyle(),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Type: ${house.houseType}", style = CC.contentTextStyle())
            Text(text = "Rooms: ${house.rooms.size}", style = CC.contentTextStyle())
            Text(text = "Category: ${house.houseLocation}", style = CC.contentTextStyle())
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Check-in Date: 2024-09-30", style = CC.contentTextStyle())
            Text(text = "Check-out Date: 2024-10-05", style = CC.contentTextStyle())
        }
    }
}