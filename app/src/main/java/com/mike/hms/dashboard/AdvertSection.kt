package com.mike.hms.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class CarouselItem(
    val itemImageLink: String
)

val carouselItems = listOf(
    CarouselItem("https://t4.ftcdn.net/jpg/01/90/38/79/240_F_190387968_wQdmFhMiZSnzHloQVa0MBgJSNstpPNFB.jpg"),
    CarouselItem("https://t4.ftcdn.net/jpg/02/45/74/65/240_F_245746541_w73mctgROl6puYGTZly2U87sPvn11mdn.jpg"),
    CarouselItem("https://t4.ftcdn.net/jpg/01/15/98/69/240_F_115986933_INnPudkCBGrZ1Da6MBmazBw2HrP7nsBv.jpg"),
    CarouselItem("https://t4.ftcdn.net/jpg/04/30/95/25/240_F_430952551_UiNxAU6GWyv4hBvuexCnJoC6poV8YxGs.jpg")
)

@Composable
fun CarouselItemCard(carouselItem: CarouselItem) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val cardHeight = screenHeight * 0.2f

    // Use Card instead of Box
    Card(
        modifier = Modifier
            .width(cardHeight * 2.0f)
            .height(cardHeight)
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp),  // Rounded corners
        elevation = CardDefaults.cardElevation(5.dp)                 // Elevation for shadow
    ) {
        AsyncImage(
            model = carouselItem.itemImageLink,
            contentDescription = "Carousel Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp)),  // Clip to rounded corners
            contentScale = ContentScale.Crop
        )
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


