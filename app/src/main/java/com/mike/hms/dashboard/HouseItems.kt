package com.mike.hms.dashboard

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
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
                .border(1.dp,
                    CC
                        .secondaryColor()
                        .copy(0.5f),
                    CircleShape
                )
        ) {
            AsyncImage(
                modifier = Modifier.size(boxSize),
                model = houseType.houseImageLink.random(),
                contentDescription = "House Type Image",
                contentScale = ContentScale.Crop,

                )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = houseType.houseType.name,
            style = CC.titleTextStyle().copy(fontSize = textSize * 0.2f)
        )
    }

}


@Composable
fun HouseTypeList(modifier: Modifier = Modifier) {
    val houseViewModel: HouseViewModel = hiltViewModel()
    val houses = houseViewModel.houses.collectAsState()
    LazyRow(
        modifier = modifier
            .padding(start = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(houses.value) { houses ->
            HouseItem(houses)
        }
    }
}
