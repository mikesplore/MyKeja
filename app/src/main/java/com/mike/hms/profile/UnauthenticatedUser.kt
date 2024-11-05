package com.mike.hms.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.authentication.GoogleAuth
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun UnauthenticatedUser(
    context: Context,
    onSignInResult: (Boolean) -> Unit,
    userViewModel: UserViewModel,
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var profilePicture by remember { mutableStateOf("") }
    var genderSelected by remember { mutableStateOf("") }

    var isAuthenticated by remember { mutableStateOf(false) }
    var isUserFound by remember { mutableStateOf(false) }
    var addUserLoading by remember { mutableStateOf(false) }
    val authenticatedUser = FirebaseAuth.getInstance().currentUser
    val authenticatedEmail = authenticatedUser?.email

    val user by userViewModel.user.collectAsState()

    // Toast helper functions
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(isAuthenticated) {
        authenticatedEmail?.let {
            userViewModel.getUserByEmail(it) // Trigger user retrieval by email
        }
        showToast("$authenticatedEmail is Authenticated, searching")
    }

    LaunchedEffect(user) {
        if (user != null) {
            isUserFound = true
        } else if (isAuthenticated) {
            isUserFound = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (authenticatedEmail == null) {
            // If not authenticated, show Google Sign-In button
            Text("Please sign in to continue", style = CC.contentTextStyle())
            Spacer(modifier = Modifier.height(16.dp))
            GoogleAuth { success ->
                if (success) {
                    isAuthenticated = true
                } else {
                    onSignInResult(false)
                }
            }
        } else {
            if (isUserFound) {
                Text("Welcome back, ${user?.firstName}")
            } else {
                // If user not found, show the form for entering details
                Text("Complete your profile", style = CC.contentTextStyle())
                UserDetailsForm(
                    firstName = firstName,
                    onFirstNameChange = { firstName = it },
                    lastName = lastName,
                    onLastNameChange = { lastName = it },
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = { phoneNumber = it },
                    genderSelected = genderSelected,
                    onGenderSelectedChange = { genderSelected = it },
                    onSubmit = {
                        if (true) {
                            CC.generateUserId { id ->
                                email = authenticatedEmail
                                val newUser = UserEntity(
                                    userID = id,
                                    firstName = firstName,
                                    lastName = lastName,
                                    gender = genderSelected,
                                    phoneNumber = phoneNumber,
                                    role = "user",
                                    email = email,
                                    photoUrl = profilePicture

                                )
                                addUserLoading = true
                                userViewModel.insertUser(newUser) { success ->
                                    addUserLoading = false
                                    if (success) {
                                        showToast("Profile completed successfully!")
                                        onSignInResult(true)
                                    } else {
                                        showToast("Failed to save profile. Please try again.")
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }

        if (addUserLoading) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun UserDetailsForm(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    genderSelected: String,
    onGenderSelectedChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = genderSelected,
            onValueChange = onGenderSelectedChange,
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }
    }
}
