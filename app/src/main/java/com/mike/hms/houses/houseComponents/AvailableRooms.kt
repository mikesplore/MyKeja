package com.mike.hms.houses.houseComponents

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun InsideView(navController: NavController, context: Context, houseID: String) {
    val houseViewModel: HouseViewModel = hiltViewModel()
    val house by houseViewModel.house.collectAsState()

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

        TextButton(onClick = { navController.navigate("houseGallery/${houseID}") }) {
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

