package com.mike.hms.dashboard

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mike.hms.homeScreen.TopAppBarComponent
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun DashboardScreen(context: Context, navController: NavController) {
    val houseViewModel: HouseViewModel = hiltViewModel()
    val houses = houseViewModel.houses.collectAsState()

    LaunchedEffect(Unit) {
        houseViewModel.getAllHouses()
        Toast.makeText(context, houses.value.size.toString(), Toast.LENGTH_SHORT).show()
    }

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
            CC.SearchTextField(
                value = "",
                onValueChange = { /* Handle search query change */ },
                placeholder = "Search destination...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            HouseTypeList(houses = houses.value)
            Spacer(modifier = Modifier.height(20.dp))
            HousesCategory()
            Spacer(modifier = Modifier.height(20.dp))
            RecommendedHouseTypeList(navController = navController)
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
                    "Popular Places",
                    style = CC.titleTextStyle().copy(
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
            PopularHouseTypeList(navController = navController)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Recommended for you",
                    style = CC.titleTextStyle().copy(
                        color = CC.secondaryColor()
                    ),
                )
                Text(
                    "See all", style = CC.contentTextStyle().copy(
                        color = CC.extraPrimaryColor()
                    )
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

        }
    }
}
