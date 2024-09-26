package com.mike.hms.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
                RescheduleCard()

                Spacer(modifier = Modifier.weight(1f))

                // Proceed to Checkout Button
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CC.extraPrimaryColor()
                    )
                ) {
                    Text(text = "Proceed to Checkout", style = CC.bodyTextStyle().copy(
                        fontWeight = FontWeight.Bold,
                        color = CC.primaryColor()
                    ))
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
                Row(
                    modifier = Modifier
                        .width(screenWidth * 0.4f)
                        .background(CC.extraSecondaryColor(), shape = RoundedCornerShape(10.dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Location Icon",
                        tint = CC.secondaryColor(),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                Text(
                    text = "Premium Member", // Membership status
                    style = CC.contentTextStyle().copy(
                        color = CC.secondaryColor(),
                        fontStyle = FontStyle.Italic,
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )}
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
            .height(screenHeight * 0.11f)
            .fillMaxWidth(0.9f)
    ) {
        Column(
            modifier = Modifier

                .background(CC.primaryColor())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    "Ksh. ${formatNumber(house.housePrice)} /Night",
                    style = CC.bodyTextStyle(),
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = CC.primaryColor()),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier

                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CharacteristicsColumn(
                    title1 = "Check-In",
                    value1 = "01/02/2024",
                    title2 = "Check-Out",
                    value2 = "11/02/2024"
                )
                CharacteristicsColumn(
                    title1 = "House Type",
                    value1 = house.houseType.toString(),
                    title2 = "Capacity",
                    value2 = "${house.rooms.size} Rooms"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TotalPriceRow()
        }
    }
}

@Composable
fun CharacteristicsColumn(
    title1: String,
    value1: String,
    title2: String,
    value2: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CharacteristicItem(title = title1, value = value1)
        CharacteristicItem(title = title2, value = value2)
    }
}

@Composable
fun CharacteristicItem(title: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(CC.extraPrimaryColor())
        )
        Column {
            Text(
                text = title,
                style = CC.bodyTextStyle().copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = value,
                style = CC.bodyTextStyle().copy(
                    color = CC.extraPrimaryColor(),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun TotalPriceRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CC.secondaryColor().copy(alpha = 0.1f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total Price",
            style = CC.bodyTextStyle().copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Ksh 45,000",
            style = CC.titleTextStyle().copy(
                fontWeight = FontWeight.ExtraBold,
                color = CC.tertiaryColor()
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleCard() {
    var oldDate by remember { mutableStateOf(LocalDate.now()) }
    var newDate by remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    var showOldDatePicker by remember { mutableStateOf(false) }
    var showNewDatePicker by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = CC.primaryColor()),
        modifier = Modifier

            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = "Reschedule",
                style = CC.titleTextStyle().copy(fontWeight = FontWeight.Bold),
                color = CC.textColor()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DateColumn(
                    title = "Old Date",
                    date = oldDate,
                    onDateClick = { showOldDatePicker = true }
                )
                DateColumn(
                    title = "New Date",
                    date = newDate,
                    onDateClick = { showNewDatePicker = true }
                )
            }
        }
    }

    // Date Pickers
    if (showOldDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showOldDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showOldDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOldDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = oldDate.toEpochDay() * 24 * 60 * 60 * 1000),

            )
        }
    }

    if (showNewDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showNewDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showNewDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = newDate.toEpochDay() * 24 * 60 * 60 * 1000),
            )
        }
    }
}

@Composable
fun DateColumn(
    title: String,
    date: LocalDate,
    onDateClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = title,
            style = CC.bodyTextStyle().copy(fontWeight = FontWeight.Medium),
            color = CC.textColor()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onDateClick)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select Date",
                tint = CC.extraPrimaryColor(),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                style = CC.bodyTextStyle().copy(fontWeight = FontWeight.Bold),
                color = CC.extraPrimaryColor()
            )
        }
    }
}

