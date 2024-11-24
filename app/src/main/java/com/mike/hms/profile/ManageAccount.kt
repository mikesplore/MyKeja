package com.mike.hms.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.HMSPreferences
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.model.userModel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAccount(userViewModel: UserViewModel, context: Context, navController: NavController){
    val user by userViewModel.user.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val email = auth.currentUser?.email
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        userViewModel.getUserByEmail(email!!)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Manage Account", style = CC.titleTextStyle()) },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = null,
                            tint = CC.textColor()
                        )
                    }
                },
                colors = CC.topAppBarColors()
            )
        }

    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()

        ) {
            user?.let { EditDetails(it, userViewModel){
                if (it){
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Details updated successfully", actionLabel = "Dismiss")
                        keyboardController?.hide()
                        snackbarHostState.currentSnackbarData?.dismiss()

                    }
                }
                else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Failed to update details")
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
            } }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                color = CC.textColor().copy(alpha = 0.5f),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {auth.signOut()},
                    colors = CC.buttonColors(),
                ) {
                    Text("Sign Out", style = CC.contentTextStyle())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                color = CC.textColor().copy(alpha = 0.5f),
                thickness = 1.dp
            )

            //Delete Account Section
            Text(
                "Danger Zone",
                style = CC.titleTextStyle().copy(
                    color = Color(0xFFDC3545),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                    ),
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)

            )
            user?.let { it1 ->
                DangerZone(
                    user = it1,
                    userViewModel = userViewModel,
                    context = context,
                    navController = navController
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                color = CC.textColor().copy(alpha = 0.5f),
                thickness = 1.dp
            )


        }
    }
}

@Composable
fun EditDetails(user: UserEntity, userViewModel: UserViewModel, feedback:(Boolean) -> Unit) {
    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    val email by remember { mutableStateOf(user.email) }
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Edit Details",
                style = CC.titleTextStyle().copy(
                    color = CC.secondaryColor(),
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            CC.MyOutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name",
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter First Name"
            )
            Spacer(modifier = Modifier.height(8.dp))

            CC.MyOutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name",
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter Last Name"
            )
            Spacer(modifier = Modifier.height(8.dp))

            CC.MyOutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone Number",
                keyboardType = KeyboardType.Phone,
                placeholder = "Enter Phone Number",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            CC.MyOutlinedTextField(
                value = email,
                onValueChange = {},
                label = "Email",
                keyboardType = KeyboardType.Email,
                placeholder = "Enter Email",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val updatedUser = user.copy(
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = phoneNumber
                    )
                    userViewModel.insertUser(updatedUser) { success ->
                        if (success) {
                            feedback(true)

                        } else {
                            feedback(false)
                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CC.secondaryColor(),
                )
            ) {
                Text("Save", style = CC.contentTextStyle().copy(color = CC.primaryColor()))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
}






