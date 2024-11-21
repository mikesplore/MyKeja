package com.mike.hms.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.HMSPreferences
import com.mike.hms.model.creditCardModel.CreditCardEntity
import com.mike.hms.model.creditCardModel.CreditCardViewModel
import com.mike.hms.model.creditCardModel.CreditCardWithUser
import com.mike.hms.model.userModel.UserViewModel
import kotlinx.coroutines.delay
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun AuthenticatedUser(
    isEditMode: Boolean,
    context: Context = LocalContext.current
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val creditCardViewModel: CreditCardViewModel = hiltViewModel()
    val email = FirebaseAuth.getInstance().currentUser?.email
    val user by userViewModel.user.collectAsState()
    val creditCard by creditCardViewModel.creditCard.collectAsState()
    var showAddCard by remember {mutableStateOf(false)}

    LaunchedEffect(HMSPreferences.userId.value) {
        val maxRetries = 3
        var attempt = 0

        while (attempt < maxRetries) {
            // Fetch the user by email
            userViewModel.getUserByEmail(email!!)
            Log.d("CreditCardViewModell", "Attempt ${attempt + 1}: Fetching credit card for user ID: ${HMSPreferences.userId.value}")

            // Fetch the credit card
            creditCardViewModel.getCreditCard(HMSPreferences.userId.value)

            // Check if the credit card is fetched (assuming creditCard is a state or flow)

            if (creditCard != null) {
                Log.d("CreditCardViewModell", "Credit card fetched successfully: $creditCard")
                break
            }

            attempt++
            delay(1000L) // Wait for 1 second before retrying
        }

        if (creditCard == null) {
            Log.d("CreditCardViewModell", "Failed to fetch credit card after $maxRetries attempts.")
        }
    }




    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // User Details Section
        Text(
            text = "Personal Information",
            style = CC.titleTextStyle(),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Editable or non-editable user information
        if (isEditMode) {
            user?.let { EditDetails(it) }
        } else {
            user?.let { UserCard(it) }
        }
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            color = CC.textColor(),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Credit Card Section
        Text(
            text = "Credit Card Information",
            style = CC.titleTextStyle(),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Conditional rendering for credit card
        if (creditCard != null) {
            CreditCard(creditCard!!,{
                creditCardViewModel.deleteCreditCard(creditCard!!.creditCard.userId)
            })
        } else {
            Row {
            Text("You don't have a saved credit card yet. ", style = CC.contentTextStyle())
            Text("Add one", style = CC.contentTextStyle().copy(color = CC.titleColor()),
                modifier = Modifier.clickable{
                    showAddCard = true
                })
            }
            Spacer(modifier = Modifier.height(10.dp))
            AnimatedVisibility(showAddCard) {
                AddCreditCard(context, creditCardViewModel) {
                    showAddCard  = it
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            color = CC.textColor(),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        // Danger Zone Section
        DangerZone(userViewModel, context, navController = NavController(context))
    }

}





@Composable
fun DangerZone(userViewModel: UserViewModel, context: Context, navController: NavController) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {

        if (showDeleteDialog) {
            ShowDeleteDialog(
                onDismiss = { showDeleteDialog = it },
                userViewModel = userViewModel,
                context = context
            )
        }
        Text("Delete Account", style = CC.contentTextStyle())
        Button(
            onClick = {
                showDeleteDialog = !showDeleteDialog

            },
            colors = CC.buttonColors().copy(),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text("Delete Account", style = CC.contentTextStyle())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDeleteDialog(
    onDismiss: (Boolean) -> Unit,
    userViewModel: UserViewModel,
    context: Context

) {
    val userID = HMSPreferences.userId.value
    BasicAlertDialog(
        onDismissRequest = { onDismiss(false) }
    ) {
        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = CC.textColor(),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(CC.primaryColor())
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Are you sure you want to delete your account?", style = CC.contentTextStyle())

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onDismiss(false) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel", style = CC.contentTextStyle())
                }
                Button(
                    onClick = {
                        userViewModel.deleteUser(userID) { success ->
                            if (success) {
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                onDismiss(true)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed, please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onDismiss(false)
                            }

                        }

                    },
                    enabled = false,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Delete", style = CC.contentTextStyle())
                }
            }
        }
    }

}







