package com.mike.hms.dashboard

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mike.hms.homeScreen.TopAppBarComponent
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun DashboardScreen(context: Context, navController: NavController){
    Scaffold(
        topBar = {
            TopAppBarComponent(context)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TopText()
            Spacer(modifier = Modifier.height(10.dp))
            CC.SearchTextField(
                value = "",
                onValueChange = { /* Handle search query change */ },
                placeholder = "Search destinations...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            HouseTypeList()
            Spacer(modifier = Modifier.height(20.dp))
            CarouselWithLoop()
        }
    }

}

@Composable
fun TopText(){
    CC.AdaptiveSizes{
        textSize, dpSize ->
        Row(modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp )
            .width(dpSize)) {
            Text("Where do you want to go?", style = CC.titleTextStyle().copy(fontSize = textSize* 0.06f, color = CC.tertiaryColor()))

        }
    }
}