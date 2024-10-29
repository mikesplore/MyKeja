package com.mike.hms.houses

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.model.houseModel.HouseViewModel
import kotlinx.coroutines.launch
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseGallery(
    navController: NavController,
    context: Context
) {
    val houseViewModel: HouseViewModel = hiltViewModel()
    val house by houseViewModel.house.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "House Gallery") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CC.primaryColor(),
                    titleContentColor = CC.textColor()
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(house?.houseImageLink?.indices?.toList() ?: emptyList()) { index ->
                ImageCard(
                    imageUrl = house?.houseImageLink?.get(index) ?: "",
                    onClick = { navController.navigate("fullScreenGallery/${index}") }
                )
            }
        }
    }
}

@Composable
fun ImageCard(imageUrl: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "House Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenGallery(navController: NavController, context: Context, houseID: String) {
    val houseViewModel: HouseViewModel = hiltViewModel()
    val house by houseViewModel.house.collectAsState()
    val images = house?.houseImageLink ?: emptyList()
    val pagerState = rememberPagerState(initialPage = 0) { images.size }
    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    LaunchedEffect(houseID) {
        houseViewModel.getHouseByID(houseID)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${pagerState.currentPage + 1} / ${images.size}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CC.primaryColor(),
                    titleContentColor = CC.textColor()
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Main image pager
            HorizontalPager(
                userScrollEnabled = true,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                ZoomableImage(imageUrl = images[page])
            }

            // Thumbnail row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(start = 20.dp)
            ) {
                items(images.size) { index ->
                    val isSelected = index == pagerState.currentPage
                    AsyncImage(
                        model = images[index],
                        contentDescription = "Thumbnail ${index + 1}",
                        modifier = Modifier
                            .size(if (isSelected) screenWidth * 0.18f else screenWidth * 0.10f)
                            .clip(RoundedCornerShape(10.dp)) // Apply clip after size
                            .padding(4.dp)
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun ZoomableImage(imageUrl: String) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    scale = scale.coerceIn(1f, 3f)

                    if (scale > 1f) {
                        offsetX += pan.x
                        offsetY += pan.y
                    } else {
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Full-size House Image",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                ),
            contentScale = ContentScale.Fit
        )
    }
}