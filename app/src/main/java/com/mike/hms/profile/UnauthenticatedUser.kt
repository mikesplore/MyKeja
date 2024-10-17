package com.mike.hms.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mike.hms.authentication.GoogleAuth
import com.mike.hms.ui.theme.CommonComponents

@Composable
fun UnauthenticatedUser(
    onSignInSuccess: () -> Unit,
    onSignInFailure: () -> Unit,

    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "You need to sign in to view your profile.",
            style = CommonComponents.contentTextStyle()
        )
        Spacer(modifier = Modifier.height(16.dp))
        GoogleAuth(
            onSignInSuccess = {
                onSignInSuccess()
            },
            onSignInFailure = {
                onSignInFailure()
            }
        )
    }
}