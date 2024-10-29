package com.mike.hms.profile

import android.content.Context
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.HMSPreferences
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(context: Context) {
    val auth = FirebaseAuth.getInstance()
    var isAuthenticated by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    val userViewModel: UserViewModel = hiltViewModel()
    val userId = HMSPreferences.userId.value


    LaunchedEffect(Unit) {
        userViewModel.getUserByID(userId)
        if (auth.currentUser != null) {
            isAuthenticated = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", style = CC.titleTextStyle()) },
                actions = {
                    if (isAuthenticated) {
                        IconButton(onClick = { isEditMode = !isEditMode }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                        }
                    }
                },
                colors = CC.topAppBarColors()
            )
        },
        containerColor = CC.primaryColor()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!isAuthenticated) {
                // Handle unauthenticated user
                UnauthenticatedUser(
                    context,
                    onSignInSuccess = {
                        isAuthenticated = true
                    },
                    onSignInFailure = {
                        isAuthenticated = false
                    }
                )

            } else {
                // Authenticated user

                AuthenticatedUser(
                    isEditMode = isEditMode,
                )
            }
        }
    }
}






