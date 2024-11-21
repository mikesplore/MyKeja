package com.mike.hms.dashboard

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mike.hms.homeScreen.TopAppBarComponent
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    context: Context,
    houses: List<HouseEntity>,
    navController: NavController,
    houseViewModel: HouseViewModel,
    userViewModel: UserViewModel
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    val textSize = with(density) { (screenWidth).toSp() }

    LaunchedEffect(Unit) {
        houseViewModel.getAllHouses()
    }

    Scaffold(
        topBar = {
            TopAppBarComponent(context, userViewModel)
        },
        containerColor = CC.primaryColor()
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .animateContentSize()
        ) {
            CC.MyOutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = "",
                label = "Search for a house",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.9f)

            )
            Spacer(modifier = Modifier.height(10.dp))
            HouseTypeList(houses = houses)
            Spacer(modifier = Modifier.height(20.dp))
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
                        fontSize = textSize * 0.04f,
                        color = CC.secondaryColor()
                    ),
                )

            }
            Spacer(modifier = Modifier.height(20.dp))
            PopularHouseTypeList(navController = navController, houses = houses, houseViewModel = houseViewModel)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
