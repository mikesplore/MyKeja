package com.mike.hms.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun ImageGallery(
    house: HouseEntity,
    onImageClick: (String?) -> Unit,
    isHouseFavorite: MutableState<Boolean>,
    screenWidth: Dp,
    screenHeight: Dp
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp)
    ) {
        items(house.houseImageLink) { imageUrl ->
            Box(
                modifier = Modifier
                    .height(screenHeight)
                    .width(screenWidth)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "House Image",
                    modifier = Modifier
                        .height(screenHeight * 0.3f)
                        .width(screenWidth)
                        .clickable { onImageClick(imageUrl) },
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { /* Handle back action */ },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = CC.primaryColor().copy(0.5f)
                    ),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Close",
                        tint = CC.tertiaryColor()
                    )
                }

                IconButton(
                    onClick = { isHouseFavorite.value = !isHouseFavorite.value },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = CC.primaryColor().copy(0.5f)
                    ),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        if (isHouseFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = CC.tertiaryColor(),
                        modifier = Modifier.size(screenWidth * 0.05f)
                    )
                }

                IconButton(
                    onClick = { /* Handle share action */ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = CC.primaryColor().copy(0.5f)
                    )
                ) {
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
}


@Composable
fun FullScreenImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
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
                onClick = { onDismiss() },
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
