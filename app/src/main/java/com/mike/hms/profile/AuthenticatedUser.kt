package com.mike.hms.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.HMSPreferences
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.profile.paymentMethods.PaymentMethodsSection
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

    listOf(
        "Credit Card",
        "PayPal",
        "M-PESA"
    )

    remember { mutableStateOf<String?>("Credit Card") }

    LaunchedEffect(HMSPreferences.userId.value) {
        val maxRetries = 3
        var attempt = 0

        while (attempt < maxRetries) {
            // Fetch the user by email
            userViewModel.getUserByEmail(email!!)
            Log.d(
                "CreditCardViewModell",
                "Attempt ${attempt + 1}: Fetching credit card for user ID: ${HMSPreferences.userId.value}"
            )

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
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        RowTopic(
            icon = Icons.Default.Person,
            text = "Edit Personal Details",
            destination = ""
        )
        Spacer(modifier = Modifier.height(20.dp))
        RowTopic(
            icon = Icons.Default.CreditCard,
            text = "Manage Credit Cards",
            destination = ""
        )
        Spacer(modifier = Modifier.height(20.dp))
        RowTopic(
            icon = Icons.Default.CollectionsBookmark,
            text = "View bookings history",
            destination = ""
        )
        Spacer(modifier = Modifier.height(20.dp))
        RowTopic(
            icon = Icons.Default.RateReview,
            text = "Ratings and Reviews",
            destination = ""
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            color = CC.textColor(),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Payment Methods Section
        user?.let { PaymentMethodsSection(it, creditCard, creditCardViewModel, context) }

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

@Composable
fun RowTopic(icon: ImageVector, text: String, destination: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = CC.textColor().copy(0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .height(screenWidth * 0.15f)
            .width(screenWidth * 0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier
                .padding(start = 10.dp)
                .size(screenWidth * 0.1f),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = CC.tertiaryColor()
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CC.textColor(),
                modifier = Modifier.size(screenWidth * 0.05f)

            )
        }

        Text(text, style = CC.contentTextStyle())

        IconButton(onClick = { /* Handle edit button click */ }) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                contentDescription = null,
                tint = CC.textColor(),
                modifier = Modifier.size(screenWidth * 0.05f)
            )
        }

    }
}

