package com.mike.hms.homeScreen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mike.hms.dashboard.DashboardScreen
import com.mike.hms.houses.favorites.Favourites
import com.mike.hms.model.creditCardModel.CreditCardViewModel
import com.mike.hms.model.favorites.FavoriteViewModel
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.profile.Profile
import kotlinx.coroutines.launch
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreen(
    houses: List<HouseEntity>,
    navController: NavController,
    context: Context,
    favoriteViewModel: FavoriteViewModel,
    houseViewModel: HouseViewModel,
    userViewModel: UserViewModel,
    creditCardViewModel: CreditCardViewModel,
) {
    val screens = listOf(
        Screen.Home, Screen.Favourites, Screen.Chat, Screen.Profile
    )
    val coroutineScope = rememberCoroutineScope()

    // Local state
    rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { screens.size })


    // Main content starts here
    if (showBottomSheet) {
        ModalBottomSheet(
            tonalElevation = 5.dp,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            },
            containerColor = CC.primaryColor(),
            sheetState = sheetState,
            content = {
                // Add your bottom sheet content here
                Text(text = "Bottom Sheet Content")
            }
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CC.primaryColor(),
            ) {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (pagerState.currentPage == index) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.name,
                                tint = if (pagerState.currentPage == index) CC.primaryColor() else CC.textColor()
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CC.primaryColor(),
                            selectedTextColor = CC.textColor(),
                            indicatorColor = CC.secondaryColor(),
                        ),
                        label = {
                            if (pagerState.currentPage == index) Text(
                                screen.name,
                                style = CC.contentTextStyle().copy(fontSize = 13.sp)
                            )
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                    )
                }
            }
        },
        containerColor = CC.primaryColor()
    ) {
        innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            HorizontalPager(
                userScrollEnabled = false,
                state = pagerState,
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
            ) { page ->
                when (screens[page]) {
                    Screen.Profile -> Profile(context, userViewModel, creditCardViewModel)
                    Screen.Favourites -> Favourites(navController, favoriteViewModel)
                    Screen.Chat -> Chat()
                    Screen.Home -> DashboardScreen(context, houses, navController, houseViewModel, userViewModel)
                }
            }
        }
    }
}


@Composable
fun Chat() {
    Box(
        modifier = Modifier

            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Chat", style = CC.titleTextStyle())
    }
}





