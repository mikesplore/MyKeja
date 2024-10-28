package com.mike.hms.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mike.hms.HMSPreferences
import com.mike.hms.model.getUserViewModel
import com.mike.hms.model.userModel.CreditCardEntity
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun AuthenticatedUser(
    isEditMode: Boolean,
    paymentMethod: String = "",
    context: Context = LocalContext.current
) {
    val userViewModel = getUserViewModel(context)
    val userID = "User7"
    val user by userViewModel.user.observeAsState()
    val creditCard by userViewModel.creditCard.observeAsState()

    LaunchedEffect(Unit) {
        userViewModel.getCreditCard(userID)
        Toast.makeText(context,"Searching credit card of user $userID", Toast.LENGTH_SHORT).show()
        userViewModel.getUserByID(userID)
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
        // Credit Card Section
        Text(
            text = "Credit Card Information",
            style = CC.titleTextStyle(),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (creditCard == null) {
            Text("You don't have a saved credit card yet", style = CC.contentTextStyle())
            Spacer(modifier = Modifier.height(16.dp))
            AddCreditCard(context, userViewModel)
            Spacer(modifier = Modifier.height(20.dp))
        } else {
            CreditCard(creditCard!!)
        }
        Spacer(modifier = Modifier.height(20.dp))
        // Danger Zone Section
        DangerZone(userViewModel, context, navController = NavController(context))
    }
}


@Composable
fun AddCreditCard(
    context: Context,
    userViewModel: UserViewModel
) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    val userID = HMSPreferences.userId.value

    var cardNumberError by remember { mutableStateOf(false) }
    var expiryDateError by remember { mutableStateOf(false) }
    var cvvError by remember { mutableStateOf(false) }
    var showAddCard by remember { mutableStateOf(false) }

    fun validateInput(): Boolean {
        var isValid = true
        cardNumberError = cardNumber.length != 16
        expiryDateError = expiryDate.isEmpty() || !expiryDate.matches("\\d{2}/\\d{2}".toRegex())
        cvvError = cvv.length != 3

        isValid = !cardNumberError && !expiryDateError && !cvvError
        return isValid
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    AnimatedVisibility(visible = !showAddCard) {
        Button(
            onClick = {
                userViewModel.getCreditCard(userID)
               // showAddCard = !showAddCard
            },
            colors = CC.buttonColors(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add Credit Card", style = CC.contentTextStyle().copy(color = CC.primaryColor()))
        }
    }

    AnimatedVisibility(visible = showAddCard) {
        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = CC.textColor(),
                    shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add Credit Card",
                style = CC.titleTextStyle(),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            CreditCardTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = "Card Number",
                keyboardType = KeyboardType.Number,
                placeholder = "1234 5678 9012 3456",
                isError = cardNumberError,
                errorMessage = "Invalid card number"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CreditCardTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = "Expiry Date",
                    keyboardType = KeyboardType.Unspecified,
                    placeholder = "MM/YY",
                    modifier = Modifier.weight(1f),
                    isError = expiryDateError,
                    errorMessage = "Invalid expiry date"
                )

                Spacer(modifier = Modifier.width(16.dp))

                CreditCardTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = "CVV",
                    placeholder = "123",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f),
                    isError = cvvError,
                    errorMessage = "Invalid CVV"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (validateInput()) {
                            CC.generateCardId { id ->
                            val card = CreditCardEntity(
                                cardId = id,
                                userId = userID,
                                cardNumber = cardNumber,
                                expiryDate = expiryDate,
                                cvv = cvv
                            )
                            userViewModel.insertCreditCard(card) { success ->
                                if (success) {
                                    showToast("Success!")
                                } else {
                                    showToast("Failure!")
                                }
                            }}
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = CC.buttonColors()
                ) {
                    Text(
                        "Save Card",
                        style = CC.contentTextStyle()
                            .copy(fontWeight = FontWeight.Bold, color = CC.primaryColor())
                    )
                }
                OutlinedButton(
                    onClick = {
                        showAddCard = !showAddCard
                    },
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        "Cancel",
                        style = CC.contentTextStyle().copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

    }
}

@Composable
fun CreditCardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    Column(modifier = modifier) {
        CC.MyOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            keyboardType = keyboardType,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder,
            isError = isError
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
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
        onDismissRequest = {onDismiss(false)}
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
                        userViewModel.deleteUser(userID){ success ->
                            if (success){
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                onDismiss(true)
                            } else {
                                Toast.makeText(context, "Failed, please try again", Toast.LENGTH_SHORT).show()
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







