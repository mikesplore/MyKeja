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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
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
import com.mike.hms.HMSPreferences
import com.mike.hms.model.getUserViewModel
import com.mike.hms.model.userModel.CreditCardEntity
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun AuthenticatedUser(
    isEditMode: Boolean,
    paymentMethod: String,
    context: Context = LocalContext.current
) {
    val userViewModel = getUserViewModel(context)
    val userID = HMSPreferences.userId.value
    val user by userViewModel.user.observeAsState()
    val creditCard by userViewModel.creditCard.observeAsState()

    LaunchedEffect(Unit) {
        userViewModel.getCreditCard(userID)
        userViewModel.getUserByID(userID)
    }


    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
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
            EditDetails()

        } else {
            user?.let { UserCard(it) }
        }

        Spacer(modifier = Modifier.height(32.dp))
        creditCard?.let {
            CreditCard(
                creditCardWithUser = it
            )
        }

        // Payment Method Section
        Text(
            text = "Payment Method",
            style = CC.titleTextStyle(),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Payment Method
        if (creditCard != null) {

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Payment Method Button
        if (paymentMethod.isNotEmpty()) {
            Button(
                onClick = {
                    // TODO: Implement logic to save payment method
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Payment Method")
            }
        }
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

    Text("Add Credit Card", style = CC.titleTextStyle())
    CC.MyOutlinedTextField(
        value = cardNumber,
        onValueChange = { cardNumber = it },
        label = "Card Number",
        keyboardType = KeyboardType.Number,
        modifier = Modifier.fillMaxWidth(),
        placeholder = "1234 5678 9012 3456"
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        CC.MyOutlinedTextField(
            value = expiryDate,
            onValueChange = { expiryDate = it },
            label = "Expiry Date",
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f),
            placeholder = "MM/YY"
        )
        Spacer(modifier = Modifier.width(16.dp))
        CC.MyOutlinedTextField(
            value = cvv,
            onValueChange = { cvv = it },
            label = "CVV",
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f),
            placeholder = "123"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = {
            val card = CreditCardEntity(
                userId = userID,
                cardNumber = cardNumber,
                expiryDate = expiryDate,
                cvv = cvv
            )
            userViewModel.insertCreditCard(card) {
                if (it) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Save Card", style = CC.contentTextStyle().copy(fontWeight = FontWeight.Bold))
        }
    }
}








