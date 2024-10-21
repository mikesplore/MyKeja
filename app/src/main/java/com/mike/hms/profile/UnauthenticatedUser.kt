package com.mike.hms.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.HMSPreferences
import com.mike.hms.authentication.GoogleAuth
import com.mike.hms.model.getUserViewModel
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun UnauthenticatedUser(
    context: Context,
    onSignInSuccess: () -> Unit,
    onSignInFailure: () -> Unit,
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var profilePicture by remember { mutableStateOf("") }
    var genderSelected by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(true) }
    var isUserFound by remember { mutableStateOf(false) }
    var isAuthenticated by remember { mutableStateOf(false) }
    var addUserLoading by remember { mutableStateOf(false) }

    val userViewModel = getUserViewModel(context)
    val userID by remember { mutableStateOf(HMSPreferences.userId.value) }
    val user by userViewModel.user.observeAsState()
    val authenticatedUser = FirebaseAuth.getInstance().currentUser


    // Fetch user data when the composable is first launched
    LaunchedEffect(userID) {
        userViewModel.getUserByID(userID)
        loading = false
    }

    // Update fields if user is found
    user?.let {
        firstName = it.firstName
        lastName = it.lastName
        email = it.email
        phoneNumber = it.phoneNumber
        profilePicture = it.photoUrl
        isUserFound = true
    }

    // Toast helper functions
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show loading indicator
        if (loading) {
            CircularProgressIndicator()
        }

        // Show Google sign-in and form if user is not found
        if (!loading && !isUserFound) {
            if (!isAuthenticated) {
                // Google Sign-In process
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Sign in with Google to get started.",
                        style = CC.contentTextStyle()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Google Sign-In Button
                    GoogleAuth(
                        onSignInSuccess = {
                            email = authenticatedUser?.email ?: ""
                            profilePicture = authenticatedUser?.photoUrl?.toString() ?: ""
                            isAuthenticated = true
                        },
                        onSignInFailure = {
                            onSignInFailure()
                        }
                    )
                }
            } else {
                // Input form for additional details
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Please enter your additional details.",
                        style = CC.titleTextStyle()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CC.MyOutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = "First Name",
                        placeholder = "Doe"
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CC.MyOutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = "Last Name",
                        placeholder = "Doe"
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CC.MyOutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = "Phone Number",
                        placeholder = "+254"
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Gender selection
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Gender", style = CC.titleTextStyle())
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(
                            selected = genderSelected == "male",
                            onClick = { genderSelected = "male" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = CC.secondaryColor()
                            )
                        )
                        Text(
                            "Male",
                            style = CC.contentTextStyle().copy(color = CC.secondaryColor())
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = genderSelected == "female",
                            onClick = { genderSelected = "female" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = CC.secondaryColor()
                            )
                        )
                        Text(
                            "Female",
                            style = CC.contentTextStyle().copy(color = CC.secondaryColor())
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Validate form fields
                            addUserLoading = true
                            if (firstName.isNotEmpty() && lastName.isNotEmpty() && phoneNumber.isNotEmpty()) {
                                CC.generateUserId { id ->
                                    val newUser = UserEntity(
                                        userID = id,
                                        firstName = firstName,
                                        lastName = lastName,
                                        gender = genderSelected,
                                        phoneNumber = phoneNumber,
                                        email = authenticatedUser?.email.toString(),
                                        photoUrl = authenticatedUser?.photoUrl.toString(),
                                        role = "user"
                                    )
                                    userViewModel.insertUser(newUser) { success ->
                                        if (success) {
                                            HMSPreferences.saveUserId(userID)
                                            onSignInSuccess()
                                            showToast("Success!")
                                            addUserLoading = true
                                        } else {
                                            onSignInFailure()
                                            showToast("Failure!")
                                            addUserLoading = false
                                        }
                                    }
                                }
                            } else {
                                addUserLoading = false
                                showToast("Please fill all details.")
                            }
                        },
                        colors = CC.buttonColors()
                    ) {

                        Row {
                            if (addUserLoading) {
                                CircularProgressIndicator(
                                    color = CC.textColor(),
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    "Submit Details",
                                    style = CC.contentTextStyle()
                                        .copy(color = CC.extraSecondaryColor())
                                )
                            }
                        }
                    }
                }
            }
        }

        // Show welcome message if user is found
        if (!loading && isUserFound) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Welcome back, $firstName!", style = CC.titleTextStyle())
            }
        }
    }
}

