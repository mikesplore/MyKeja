package com.mike.hms.houses

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.model.getHouseViewModel
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun InsideView(navController: NavController, context: Context, houseID: String) {
    val houseViewModel = getHouseViewModel(context)
    val house by houseViewModel.house.observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, bottom = 10.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Inside View",
            style = CC.titleTextStyle().copy(color = CC.tertiaryColor())
        )

        Button(
            onClick = { navController.navigate("houseGallery/$houseID") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Open",
                    style = CC.contentTextStyle().copy(color = CC.extraPrimaryColor())
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Open",
                    tint = CC.extraPrimaryColor()
                )
            }
        }


    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(house?.houseImageLink?.size ?: 0) {
            val imageUrl = house?.houseImageLink?.get(it)

            AsyncImage(
                model = imageUrl,
                contentDescription = "House Image",
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

        }

    }
}

