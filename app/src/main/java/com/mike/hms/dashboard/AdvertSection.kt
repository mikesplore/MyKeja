package com.mike.hms.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.CarouselItem
import com.mike.hms.homeScreen.carouselItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun CarouselItemCard(carouselItem: CarouselItem) {
    BoxWithConstraints {
        val cardHeight = maxHeight * 0.3f
        Box(
            modifier = Modifier
                .width(cardHeight * 2.0f)
                .height(cardHeight)
                .padding(10.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 1.dp,
                    color = CC.secondaryColor(),
                    shape = RoundedCornerShape(20.dp)
                )
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(20.dp)
                )

        ) {
            AsyncImage(
                model = carouselItem.itemImageLink,
                contentDescription = "Carousel Image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,

                )

        }
    }
}

@Composable
fun CarouselWithLoop() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Virtually extend the list by duplicating items
    val infiniteItems = List(1000) { index -> carouselItems[index % carouselItems.size] }

    LaunchedEffect(Unit) {
        // Start from the middle of the list to allow smooth infinite scrolling
        listState.scrollToItem(infiniteItems.size / 2)

        while (true) {
            delay(3000)
            coroutineScope.launch {
                val currentIndex = listState.firstVisibleItemIndex
                val nextIndex = currentIndex + 1

                listState.animateScrollToItem(nextIndex)

                // Reset the list when it reaches the end to simulate an infinite loop
                if (nextIndex >= infiniteItems.size - carouselItems.size) {
                    val middleIndex = infiniteItems.size / 2
                    listState.scrollToItem(middleIndex - (carouselItems.size / 2))
                }
            }
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        items(infiniteItems.size) { index ->
            val item = infiniteItems[index]
            CarouselItemCard(carouselItem = item)
        }
    }
}


