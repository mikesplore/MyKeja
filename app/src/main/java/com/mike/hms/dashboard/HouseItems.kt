package com.mike.hms.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.model.houseModel.HouseType
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun HouseItem(houseType: HouseType){
    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier

            .border(
                width = 1.dp,
                color = CC.secondaryColor(),
                shape = CircleShape
            )
            .clip(CircleShape)
            .size(70.dp)){
            AsyncImage(
                model = houseType.houseTypeImageLink,
                contentDescription = houseType.houseType,
                modifier = Modifier.size(70.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(houseType.houseType, style = CC.bodyTextStyle())
    }
}

@Composable
fun HouseTypeList(){
    LazyRow(
        modifier = Modifier.padding(horizontal = 10.dp)
    ){
        items(houseTypes){ houseType ->
            HouseItem(houseType)
        }

    }
}