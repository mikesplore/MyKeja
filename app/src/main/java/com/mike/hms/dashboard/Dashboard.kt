package com.mike.hms.dashboard

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mike.hms.homeScreen.TopAppBarComponent
import com.mike.hms.model.roomModel.RoomCategory
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun DashboardScreen(context: Context, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBarComponent(context)
        },
        containerColor = CC.primaryColor()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TopText()
            Spacer(modifier = Modifier.height(10.dp))
            CC.SearchTextField(
                value = "",
                onValueChange = { /* Handle search query change */ },
                placeholder = "Search destination...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            HouseTypeList()
            Spacer(modifier = Modifier.height(20.dp))
            RoomsCategory()
            Spacer(modifier = Modifier.height(20.dp))
            RecommendedRoomTypeList()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Offers for you", style = CC.titleTextStyle().copy(
                    color = CC.secondaryColor()
                ),
                modifier = Modifier.padding(start = 20.dp)
            )
            CarouselWithLoop()
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Text(
                "Popular Places", style = CC.titleTextStyle().copy(
                    color = CC.secondaryColor()
                ),
            )
                Text(
                    "See all", style = CC.contentTextStyle().copy(
                        color = CC.extraPrimaryColor()
                    )
                )

            }
            Spacer(modifier = Modifier.height(20.dp))
            PopularHouseTypeList()
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Recommended for you", style = CC.titleTextStyle().copy(
                        color = CC.secondaryColor()
                    ),
                )
                Text(
                    "See all", style = CC.contentTextStyle().copy(
                        color = CC.extraPrimaryColor()
                    ))
            }
            Spacer(modifier = Modifier.height(30.dp))

        }
    }
}


@Composable
fun TopText() {
    CC.AdaptiveSizes { textSize, dpSize ->
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .width(dpSize)
        ) {
            Text(
                "Where do you want to go?",
                style = CC.titleTextStyle()
                    .copy(fontSize = textSize * 0.06f, color = CC.tertiaryColor())
            )

        }
    }
}