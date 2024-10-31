package com.mike.hms.houses

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.model.favorites.FavoriteViewModel
import com.mike.hms.model.favorites.FavouriteEntity
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@SuppressLint("UnrememberedMutableState")
@Composable
fun HouseImageOverView(
    context: Context,
    house: HouseEntity,
    onImageClick: (String?) -> Unit,
    isHouseFavorite: MutableState<Boolean>,
    screenHeight: Dp,
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    houseViewModel: HouseViewModel = hiltViewModel(),

    ) {
    // State for the current page index
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { house.houseImageLink.size })

    // Image index state that updates when pager changes
    val currentImageIndex by derivedStateOf { pagerState.currentPage }

    val isFavorite by houseViewModel.isFavorite.collectAsState()

    LaunchedEffect(key1 = isFavorite) {
        houseViewModel.isFavorite(house.houseID)
        isHouseFavorite.value = isFavorite
    }



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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.4f))
                .align(Alignment.TopCenter)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .background(
                        Color.Transparent
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }
            IconButton(onClick = {
                isHouseFavorite.value = !isHouseFavorite.value
                if (isHouseFavorite.value) {
                    // Add to favorites
                    CC.generateFavouriteId { favId ->
                        val favouriteEntity = FavouriteEntity(
                            favId,
                            house.houseID
                        )
                        favoriteViewModel.addFavorite(favouriteEntity)
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    // Remove from favorites
                    favoriteViewModel.removeFavorite(house.houseID)
                }
            }) {
                Icon(
                    imageVector = if (isHouseFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isHouseFavorite.value) Color.Green else Color.White,
                    modifier = Modifier.animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                )
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${currentImageIndex + 1}/${house.houseImageLink.size}",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }


        }

    }
}
