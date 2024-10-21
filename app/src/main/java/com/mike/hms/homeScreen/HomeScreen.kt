package com.mike.hms.homeScreen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.mike.hms.profile.Profile
import kotlinx.coroutines.launch
import com.mike.hms.ui.theme.CommonComponents as CC

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreen(
    navController: NavController,
    context: Context,
) {
    val screens = listOf(
        Screen.Home, Screen.Favourites, Screen.Chat, Screen.Profile
    )
    val coroutineScope = rememberCoroutineScope()

    // Local state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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
            }
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CC.primaryColor()
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
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                userScrollEnabled = false,
                state = pagerState,
                modifier = Modifier.padding(10.dp),
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
            ) { page ->
                when (screens[page]) {
                    Screen.Home -> Profile(context)
                    Screen.Favourites -> Favourites(context)
                    Screen.Chat -> Chat(context)
                    Screen.Profile -> Profile(context)
                }
            }
        }
    }

}


@Composable
fun Favourites(context: Context) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Favourites", style = CC.titleTextStyle())
    }
}


@Composable
fun Chat(context: Context) {
    Box(
        modifier = Modifier

            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Chat", style = CC.titleTextStyle())
    }
}





