package com.mike.hms.profile

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(context: Context) {
    val auth = FirebaseAuth.getInstance()
    var isAuthenticated by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var paymentPhoneNumber by remember { mutableStateOf("") }
    var isEditMode by remember { mutableStateOf(false) }
    var paymentMethod by remember { mutableStateOf("") }
    val user = auth.currentUser

    LaunchedEffect(Unit) {
        isAuthenticated = auth.currentUser != null
        if (auth.currentUser != null) {
            // Fetch user details from Firebase and populate the variables
            // This is a placeholder. You should implement the actual fetching logic.
            firstName = "John"
            lastName = "Doe"
            phoneNumber = "+1234567890"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", style = CC.titleTextStyle()) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!isAuthenticated) {
                // Handle unauthenticated user
                UnauthenticatedUser(
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
                    context = context,
                    firstName = firstName,
                    onFirstNameChange = { firstName = it },
                    lastName = lastName,
                    onLastNameChange = { lastName = it },
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = { phoneNumber = it },
                    cardNumber = cardNumber,
                    onCardNumberChange = { cardNumber = it },
                    expiryDate = expiryDate,
                    onExpiryDateChange = { expiryDate = it },
                    cvv = cvv,
                    onCvvChange = { cvv = it },
                    paymentPhoneNumber = paymentPhoneNumber,
                    onPaymentPhoneNumberChange = { paymentPhoneNumber = it },
                    isEditMode = isEditMode,
                    onEditModeChange = { isEditMode = it },
                    paymentMethod = paymentMethod,
                    onPaymentMethodChange = { paymentMethod = it },
                )
            }
        }
    }
}






