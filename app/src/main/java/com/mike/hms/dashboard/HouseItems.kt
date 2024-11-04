package com.mike.hms.dashboard

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.model.houseModel.HouseEntity
import java.util.Locale
import com.mike.hms.ui.theme.CommonComponents as CC


@Composable
fun HouseItem(houseType: HouseEntity) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val boxSize = screenWidth * 0.15f
    val density = LocalDensity.current
    val textSize = with(density) { (boxSize).toSp() }



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .clip(CircleShape)
                .border(
                    1.dp,
                    CC
                        .secondaryColor()
                        .copy(0.5f),
                    CircleShape
                )
        ) {
            AsyncImage(
                modifier = Modifier.size(boxSize),
                model = houseType.houseImageLink.firstOrNull(),
                contentDescription = "House Type Image",
                contentScale = ContentScale.Crop,

                )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = houseType.houseType.name.lowercase().capitalize(Locale.ROOT),
            style = CC.titleTextStyle().copy(fontSize = textSize * 0.2f)
        )
    }

}


@Composable
fun HouseTypeList(modifier: Modifier = Modifier, houses: List<HouseEntity>) {
    Box(
        modifier = modifier
            .animateContentSize(animationSpec = tween(500))
            .fillMaxWidth(),
        contentAlignment = Alignment.Center // Center the content of the Box
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(15.dp) // Space items by 15.dp
        ) {
            items(houses) { house ->
                HouseItem(house)
            }
        }
    }
}