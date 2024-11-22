package com.mike.hms.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.authentication.GoogleAuth
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.profile.paymentMethods.AddCreditCard
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun UnauthenticatedUser(
    context: Context,
    onSignInResult: (Boolean) -> Unit,
    userViewModel: UserViewModel,
    cardViewModel: CreditCardViewModel,
) {
    val authenticatedUser = FirebaseAuth.getInstance().currentUser
    val authenticatedEmail = authenticatedUser?.email
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var profilePicture = authenticatedUser?.photoUrl.toString()
    var genderSelected by remember { mutableStateOf("") }

    var isAuthenticated by remember { mutableStateOf(false) }
    var isUserFound by remember { mutableStateOf(false) }
    var addUserLoading by remember { mutableStateOf(false) }


    val user by userViewModel.user.collectAsState()

    // Toast helper functions
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(isAuthenticated) {
        authenticatedEmail?.let {
            userViewModel.getUserByEmail(it) // Trigger user retrieval by email
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            isUserFound = true
            Log.d("UserViewModel", "User by email: ${user?.email} found: ${user?.lastName}")
        } else if (isAuthenticated) {
            isUserFound = false
        }
    }

    Column(
        modifier = Modifier
           //.background(brush)
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
                onSignInResult(true)
            } else {
                // If user not found, show the form for entering details
                Text("Complete your profile", style = CC.titleTextStyle())

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
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(0.9f),
                ) {
                Text("Credit Card", style = CC.titleTextStyle().copy(color = CC.titleColor()))}
                Spacer(modifier = Modifier.height(16.dp))
                user?.let { CreditCardInfo(it, cardViewModel) }
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
    // State to track if the error message should be shown
    var showError by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "User Details",
            style = CC.titleTextStyle().copy(color = CC.titleColor()),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )

        // Error message displayed conditionally
        AnimatedVisibility(visible = showError) {
            Text(
                text = "Please fill in all fields",
                style = CC.contentTextStyle().copy(color = Color.Red),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )
        }

        // Input fields
        CC.MyOutlinedTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            label = "First Name",
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Enter your first name"
        )

        CC.MyOutlinedTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = "Last Name",
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Enter your last name"
        )

        CC.MyOutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = "Phone Number",
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Enter your phone number"
        )

        Text(
            text = "Select your gender",
            style = CC.titleTextStyle().copy(color = CC.secondaryColor()),
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.Start)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onGenderSelectedChange("Male") }
            ) {
                RadioButton(
                    selected = genderSelected == "Male",
                    onClick = { onGenderSelectedChange("Male") },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = CC.secondaryColor(),
                        unselectedColor = CC.textColor()
                    )
                )
                Text("Male", style = CC.contentTextStyle())
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onGenderSelectedChange("Female") }
            ) {
                RadioButton(
                    selected = genderSelected == "Female",
                    onClick = { onGenderSelectedChange("Female") },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = CC.secondaryColor(),
                        unselectedColor = CC.textColor()
                    )
                )
                Text("Female", style = CC.contentTextStyle())
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onGenderSelectedChange("Gay") }
            ) {
                RadioButton(
                    selected = genderSelected == "Gay",
                    onClick = { onGenderSelectedChange("Gay") },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = CC.secondaryColor(),
                        unselectedColor = CC.textColor()
                    )
                )
                Text("Gay (Really?)", style = CC.contentTextStyle())
            }
        }

        // Save Profile button
        Button(
            onClick = {
                // Validation logic
                if (firstName.isNotEmpty() &&
                    lastName.isNotEmpty() &&
                    phoneNumber.isNotEmpty() &&
                    genderSelected.isNotEmpty()
                ) {
                    showError = false // Hide error message
                    onSubmit()       // Trigger submission action
                } else {
                    showError = true // Show error message
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = CC.buttonColors()
        ) {
            Text("Save Profile", style = CC.contentTextStyle().copy(color = CC.primaryColor()))
        }
        HorizontalDivider(
            color = CC.secondaryColor(),
            thickness = 1.dp,
        )
    }
}




//Add Credit card
@Composable
fun CreditCardInfo(userEntity: UserEntity, cardViewModel: CreditCardViewModel) {
    var showAddCard by remember { mutableStateOf(false) }
    var dismissed by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(CC.secondaryColor(), shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = if (dismissed)
                "You can add a credit card anytime by tapping the button below."
            else
                "Would you like to add a credit card now?",
            style = CC.contentTextStyle().copy(color = CC.primaryColor()),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        AnimatedVisibility(visible = !dismissed) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showAddCard = !showAddCard },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CC.buttonColors()
                ) {
                    Text(
                        if (showAddCard) "Cancel" else "Yes",
                        style = CC.contentTextStyle().copy(
                            color = CC.primaryColor(),
                            fontSize = 14.sp
                        )
                    )
                }

                Button(
                    onClick = { dismissed = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CC.primaryColor().copy(alpha = 0.5f),
                        contentColor = CC.secondaryColor()
                    )
                ) {
                    Text(
                        "Later",
                        style = CC.contentTextStyle().copy(fontSize = 14.sp)
                    )
                }
            }
        }

        AnimatedVisibility(visible = dismissed) {
            Button(
                onClick = { showAddCard = !showAddCard },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(12.dp),
                colors = CC.buttonColors()
            ) {
                Text(
                    if (showAddCard) "Cancel" else "Add Credit Card",
                    style = CC.contentTextStyle().copy(
                        color = CC.primaryColor(),
                        fontSize = 14.sp
                    )
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    // Show AddCreditCard form when `showAddCard` is true
    AnimatedVisibility(
        visible = showAddCard,
        exit = fadeOut(animationSpec = tween(200)),
        enter = fadeIn(animationSpec = tween(200))
    ) {
        AddCreditCard(context, cardViewModel){
            showAddCard = it
        }
    }
}
