package com.mike.hms.homeScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.HMSPreferences
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComponent(context: Context, userViewModel: UserViewModel) {
    val location = HMSPreferences.location.value
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val user by userViewModel.user.collectAsState()
    val currentUserEmail = auth.currentUser?.email.toString()

    LaunchedEffect(Unit) {
        userViewModel.getUserByEmail(currentUserEmail)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(CC.primaryColor())
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
                .height(this.maxHeight * 0.08f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Title and Location Column
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Hi, ${user?.firstName}!",
                        style = TextStyle(
                            color = CC.textColor(),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = CC.secondaryColor(),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = location,
                        style = CC.contentTextStyle().copy(
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp
                        )
                    )
                }
            }

            // Profile Picture
            ProfilePicture(
                imageUrl = auth.currentUser?.photoUrl.toString(),
                size = 40.dp,
                onClick = {
                    HMSPreferences.saveDarkModePreference(!HMSPreferences.darkMode.value)
                }
            )
        }
    }
}


@Composable
fun ProfilePicture(imageUrl: String, size: Dp, onClick: () -> Unit) {
    IconButton(
        onClick,
        modifier = Modifier
            .padding(10.dp)
            .clickable { onClick() }
            .size(size)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = CC.secondaryColor(),
                shape = CircleShape
            )
    ) {
        if (imageUrl.isNullOrEmpty()) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                tint = CC.textColor()
            )
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}