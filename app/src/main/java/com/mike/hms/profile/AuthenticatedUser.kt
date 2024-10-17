package com.mike.hms.profile

import android.content.Context
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser
import com.mike.hms.HMSPreferences
import com.mike.hms.model.getUserViewModel
import com.mike.hms.model.userModel.CreditCard
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun AuthenticatedUser(
    context: Context,
    creditCard: CreditCard,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    expiryDate: String,
    onExpiryDateChange: (String) -> Unit,
    cvv: String,
    onCvvChange: (String) -> Unit,
    paymentPhoneNumber: String,
    onPaymentPhoneNumberChange: (String) -> Unit,
    isEditMode: Boolean,
    onEditModeChange: (Boolean) -> Unit,
    paymentMethod: String,
    onPaymentMethodChange: (String) -> Unit,
    user: FirebaseUser?,
) {
    val userViewModel = getUserViewModel(context)
    val userID = HMSPreferences.userId.value

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .imePadding()
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Image
        AsyncImage(
            model = user?.photoUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = CC.textColor(),
                    shape = CircleShape
                )
                .size(140.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(24.dp))

        // User Details Section
        Text(
            text = "Personal Information",
            style = CC.titleTextStyle(),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Editable or non-editable user information
        if (isEditMode) {
            // Editable fields
            EditDetails(
                firstName = firstName,
                onFirstNameChange = onFirstNameChange,
                lastName = lastName,
                onLastNameChange = onLastNameChange,
                phoneNumber = phoneNumber,
                onPhoneNumberChange = onPhoneNumberChange
            )

        } else {

        }

        Spacer(modifier = Modifier.height(24.dp))

        // Update Profile Button
        if (isEditMode) {
            Button(
                onClick = {
                    // TODO: Implement logic to update user details
                    onEditModeChange(false)
                },
                colors = CC.buttonColors(),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    "Save Changes", style = CC.contentTextStyle().copy(
                        fontWeight = FontWeight.Bold,
                        color = CC.primaryColor()
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Payment Method Section
        Text(
            text = "Payment Method",
            style = CC.titleTextStyle(),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Payment Method Radio Buttons
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = paymentMethod == "credit_card",
                onClick = { onPaymentMethodChange("credit_card") },
                colors = CC.radioButtonColors()
            )
            Text("Credit Card", style = CC.contentTextStyle())
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = paymentMethod == "phone_number",
                onClick = { onPaymentMethodChange("phone_number") },
                colors = CC.radioButtonColors()
            )
            Text("Phone Number", style = CC.contentTextStyle())
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Method Input Fields
        when (paymentMethod) {
            "credit_card" -> {
                CC.MyOutlinedTextField(
                    value = cardNumber,
                    onValueChange = { onCardNumberChange(it) },
                    label = "Card Number",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "1234 5678 9012 3456"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    CC.MyOutlinedTextField(
                        value = expiryDate,
                        onValueChange = { onExpiryDateChange(it) },
                        label = "Expiry Date",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        placeholder = "MM/YY"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    CC.MyOutlinedTextField(
                        value = cvv,
                        onValueChange = { onCvvChange(it) },
                        label = "CVV",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        placeholder = "123"
                    )
                }
            }

            "phone_number" -> {
                CC.MyOutlinedTextField(
                    value = paymentPhoneNumber,
                    onValueChange = { onPaymentPhoneNumberChange(it) },
                    label = "Phone Number",
                    keyboardType = KeyboardType.Phone,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "+254"
                )
            }
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









