package com.mike.hms.houses

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@SuppressLint("UnrememberedMutableState")
@Composable
fun HouseImageOverView(
    house: HouseEntity,
    onImageClick: (String?) -> Unit,
    isHouseFavorite: MutableState<Boolean>,
    screenHeight: Dp
) {
    // State for the current page index
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { house.houseImageLink.size })

    // Image index state that updates when pager changes
    val currentImageIndex by  derivedStateOf { pagerState.currentPage }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.25f)
    ) {
        // Image Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize()
        ) { page ->
            AsyncImage(
                model = house.houseImageLink[page],
                contentDescription = "House Image ${page + 1}",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onImageClick(house.houseImageLink[page]) },
                contentScale = ContentScale.Crop
            )
        }

        // Image Counter
        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .align(Alignment.TopEnd)
        ) {
            Text(
                text = "${currentImageIndex + 1}/${house.houseImageLink.size}",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        // Bottom Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CC.primaryColor().copy(alpha = 0.6f))
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle back action */ }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CC.textColor()
                )
            }

            IconButton(onClick = { isHouseFavorite.value = !isHouseFavorite.value }) {
                Icon(
                    if (isHouseFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = CC.textColor()
                )
            }

            IconButton(onClick = { /* Handle share action */ }) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share",
                    tint = CC.textColor()
                )
            }
        }
    }
}
