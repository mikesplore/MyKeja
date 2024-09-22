package com.mike.hms.homeScreen

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mike.hms.HMSPreferences
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComponent(context: Context){
    val brush = Brush.horizontalGradient(
        colors = listOf(CC.primaryColor(), CC.secondaryColor())
    )
    val location = HMSPreferences.location.value
    TopAppBar(
        title = {
            Column {
                Text("Good Morning, Mike!", style = CC.titleTextStyle())
                Row {
                    Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = CC.secondaryColor(), modifier = Modifier.size(16.dp))
                    Text(location, style = CC.bodyTextStyle().copy(color = CC.extraPrimaryColor()))
                }
            }
        },
        actions = {
            BoxWithConstraints {
                val maxWidth = this.maxWidth
                Box(modifier = Modifier
                    .size(maxWidth * 0.10f)
                    .clip(CircleShape)
                ){
                    ProfilePicture(
                        imageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&",
                        size = maxWidth * 0.10f,
                        onClick = {
                            HMSPreferences.darkMode.value = !HMSPreferences.darkMode.value
                        }


                    )
                }
            }
        },
        colors = CC.topAppBarColors()
    )

}


@Composable
fun ProfilePicture(imageUrl: String, size: Dp, onClick: () -> Unit) {
    Box(modifier = Modifier
        .clickable { onClick() }
        .size(size)
        .clip(CircleShape)
        .border(
            width = 1.dp,
            color = CC.secondaryColor(),
            shape = CircleShape
        )
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

}