package com.mike.hms.homeScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val selectedIcon: ImageVector, val unselectedIcon: ImageVector, val name: String
) {
    data object Home : Screen(
        Icons.Filled.Home, Icons.Outlined.Home, "Home"
    )

    data object Favourites : Screen(
        Icons.Filled.Favorite, Icons.Outlined.Favorite, "Favourites"
    )

    data object Payment : Screen(
        Icons.Filled.Payment, Icons.Outlined.Payment, "Payment"
    )

    data object Chat : Screen(
        Icons.Filled.ChatBubble, Icons.Outlined.ChatBubble, "Chat"
    )

    data object Profile : Screen(
        Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle, "Profile"
    )


}