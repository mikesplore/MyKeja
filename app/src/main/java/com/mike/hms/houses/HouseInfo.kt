package com.mike.hms.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailScreen(
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val density = LocalDensity.current.density
    val textSize = (screenWidth.value / density).sp
    val isHouseFavorite = remember { mutableStateOf(false) }


    val house = houseTypes.random()
    var selectedImage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .background(CC.primaryColor())
            .padding(top = 16.dp)
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
                Box(modifier = Modifier
                    .height(screenHeight)
                    .width(screenWidth)){
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "House Image",
                        modifier = Modifier
                            .height(screenHeight * 0.3f)
                            .width(screenWidth)
                            .clickable { selectedImage = imageUrl },
                        contentScale = ContentScale.Crop
                    )
                    IconButton(onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = CC.primaryColor()
                        ),
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .align(Alignment.TopStart)) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Close",
                            tint = CC.tertiaryColor()
                        )
                    }

                    IconButton(
                        onClick = {
                            isHouseFavorite.value = !isHouseFavorite.value
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = CC.primaryColor()
                        ),
                        modifier = Modifier
                            .align(Alignment.TopEnd)) {
                        Icon(
                            if (isHouseFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = CC.tertiaryColor(),
                            modifier = Modifier.size(screenWidth * 0.05f)
                        )
                    }
                    IconButton(onClick = { },
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = CC.primaryColor()
                        )) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = CC.tertiaryColor(),
                            modifier = Modifier.size(screenWidth * 0.05f)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = house.houseName,
                style = CC.titleTextStyle().copy(fontSize = textSize * 0.15f)
            )
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