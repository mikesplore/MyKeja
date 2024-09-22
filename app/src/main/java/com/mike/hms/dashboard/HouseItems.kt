package com.mike.hms.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC


@Composable
fun HouseItem(houseType: HouseEntity, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier) {
        val boxSize = maxHeight * 0.09f // Adjust the fraction as needed
        val density = LocalDensity.current
        val textSize = with(density) { (boxSize).toSp() }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .clip(CircleShape)
                    .border(1.dp, CC.secondaryColor(), CircleShape)
            ) {
                AsyncImage(
                    modifier = Modifier.size(boxSize),
                    model = houseType.houseImageLink,
                    contentDescription = "House Type Image",
                    contentScale = ContentScale.Crop,

                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = houseType.houseType,
                style = CC.titleTextStyle().copy(fontSize = textSize * 0.2f)
            )
        }
    }
}


@Composable
fun HouseTypeList(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .padding(start = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(houseTypes) { houseType ->
            HouseItem(houseType)
        }
    }
}
