package com.mike.hms.profile

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mike.hms.HMSPreferences
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun AuthenticatedUser(
    navController: NavController,
) {
    val userViewModel: UserViewModel = hiltViewModel()

    val user by userViewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getUserByID(HMSPreferences.userId.value)

    }

    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxSize()
            .imePadding()
    ) {
        // Fixed UserCard at the top
        user?.let {
            UserCard(
                modifier = Modifier
                    .fillMaxWidth() // UserCard spans the full width
                    .padding(16.dp),
                user = it
            )
        }

        // LazyColumn for scrollable content
        LazyColumn(
            modifier = Modifier
                .animateContentSize()
                .imePadding()
                .fillMaxWidth() // Match width
                .weight(1f) // Fills remaining height within the Column
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                HorizontalDivider(
                    color = CC.textColor().copy(0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Row Topics
            item {
                RowTopic(
                    icon = Icons.Default.ManageAccounts,
                    text = "Manage Account",
                    navController = navController,
                    destination = "manageAccount"
                )
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                RowTopic(
                    icon = Icons.Default.CreditCard,
                    text = "My Wallet",
                    navController = navController,
                    destination = "wallet"
                )
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                RowTopic(
                    icon = Icons.Default.CollectionsBookmark,
                    text = "Bookings History",
                    navController = navController,
                    destination = ""
                )
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                RowTopic(
                    icon = Icons.Default.RateReview,
                    text = "Ratings and Reviews",
                    navController = navController,
                    destination = "reviews"
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                HorizontalDivider(
                    color = CC.textColor().copy(0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }


        }
    }
}


@Composable
fun RowTopic(icon: ImageVector, text: String, navController: NavController, destination: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Row(
        modifier = Modifier
            .clickable {
                if (destination.isNotEmpty()) {
                    navController.navigate(destination)
                }
            }
            .border(
                width = 1.dp,
                color = CC.textColor().copy(0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .height(screenWidth * 0.15f)
            .width(screenWidth * 0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier
                .padding(start = 10.dp)
                .size(screenWidth * 0.1f),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = CC.tertiaryColor()
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CC.primaryColor(),
                modifier = Modifier.size(screenWidth * 0.05f)
            )
        }

        Text(text, style = CC.contentTextStyle())

        IconButton(onClick = {
            if (destination.isNotEmpty()) {
                navController.navigate(destination)
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                contentDescription = null,
                tint = CC.textColor(),
                modifier = Modifier.size(screenWidth * 0.05f)
            )
        }
    }
}

