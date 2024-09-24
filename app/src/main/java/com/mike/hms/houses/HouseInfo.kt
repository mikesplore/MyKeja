package com.mike.hms.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.houseTypes
import java.util.Locale
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailScreen(
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val house = houseTypes[0]
    var selectedImage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(house.houseName, style = CC.titleTextStyle()) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CC.primaryColor(),
                    titleContentColor = CC.primaryColor(),
                    navigationIconContentColor = CC.secondaryColor()
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Image Gallery
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
            ) {
                items(house.houseImageLink) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "House Image",
                        modifier = Modifier
                            .height(screenHeight * 0.3f)
                            .width(screenWidth)
                            .padding(horizontal = 4.dp)
                            .clickable { selectedImage = imageUrl },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // House Details
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Type: ${house.houseType.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }}",
                    style = CC.bodyTextStyle()
                )
                Text(
                    "Location: ${house.houseLocation}",
                    style = CC.contentTextStyle()
                )
                Text(
                    "Rating: ${house.houseRating}",
                    style = CC.contentTextStyle()
                )
                Text(
                    "Price: $${house.housePrice}",
                    style = CC.contentTextStyle()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Description:",
                    style = CC.bodyTextStyle()
                )
                Text(
                    house.houseDescription,
                    style = CC.contentTextStyle()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Available Rooms:",
                    style = CC.bodyTextStyle()
                )
                house.rooms.forEach { room ->
                    Text("• $room", style = CC.contentTextStyle())
                }
            }
        }
    }

    // Full-screen image dialog
    selectedImage?.let { imageUrl ->
        Dialog(onDismissRequest = { selectedImage = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Full-size House Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                IconButton(
                    onClick = { selectedImage = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CC.secondaryColor()
                    )
                }
            }
        }
    }
}