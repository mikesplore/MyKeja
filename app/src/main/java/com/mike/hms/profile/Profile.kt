package com.mike.hms.profile

import android.content.Context
import android.widget.Toast
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
fun Profile(context: Context, userViewModel: UserViewModel) {
    val auth = FirebaseAuth.getInstance()
    var isAuthenticated by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    val userViewModel: UserViewModel = hiltViewModel()
    val user by userViewModel.user.collectAsState()
    val userEmail  = auth.currentUser?.email

    LaunchedEffect(Unit) {
        userEmail?.let {
            Toast.makeText(context, "Email found, searching", Toast.LENGTH_SHORT).show()
            userViewModel.getUserByEmail(it)}
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
            if (isAuthenticated) {
                Text("${user?.email} is Authenticated")

            } else {
                UnauthenticatedUser(context = context, userViewModel = userViewModel, onSignInResult = {

                })
            }
        }
    }
}






