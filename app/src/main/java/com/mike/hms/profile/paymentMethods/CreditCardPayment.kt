package com.mike.hms.profile.paymentMethods

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mike.hms.HMSPreferences
import com.mike.hms.model.paymentMethods.CreditCardEntity
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.ui.theme.CommonComponents as CC


@Composable
fun AddCreditCard(
    context: Context,
    creditCardViewModel: CreditCardViewModel,
    onShowAddCard: (Boolean) -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    val userID = HMSPreferences.userId.value

    var cardNumberError by remember { mutableStateOf(false) }
    var expiryDateError by remember { mutableStateOf(false) }
    var cvvError by remember { mutableStateOf(false) }

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
                            creditCardViewModel.insertCreditCard(card) { success ->
                                if (success) {
                                    showToast("Success!")
                                    creditCardViewModel.getCreditCard(userID)
                                } else {
                                    showToast("Failure!")
                                }
                            }
                        }
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
                    onShowAddCard(false)
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