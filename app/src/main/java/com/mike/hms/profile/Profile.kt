package com.mike.hms.profile

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.model.creditCardModel.CreditCardViewModel
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    context: Context,
    userViewModel: UserViewModel,
    creditCardViewModel: CreditCardViewModel
) {
    val auth = FirebaseAuth.getInstance()
    var isAuthenticated by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    val userEmail = auth.currentUser?.email

    LaunchedEffect(Unit) {
        userEmail?.let {
            userViewModel.getUserByEmail(it)
        }
    }
    Brush.verticalGradient(
        listOf(CC.primaryColor(), CC.titleColor().copy(0.5f), CC.tertiaryColor(), CC.primaryColor())
    )

    Scaffold(
        containerColor = CC.primaryColor()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .animateContentSize()
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isAuthenticated) {
                AuthenticatedUser(
                    isEditMode = isEditMode
                )


            } else {
                UnauthenticatedUser(context = context,
                    cardViewModel = creditCardViewModel,
                    userViewModel = userViewModel, onSignInResult = {
                        isAuthenticated = it
                    }
                )
            }
        }
    }
}






