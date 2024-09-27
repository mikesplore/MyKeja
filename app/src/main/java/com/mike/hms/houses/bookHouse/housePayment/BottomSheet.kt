package com.mike.hms.houses.bookHouse.housePayment

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mike.hms.R
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentBottomSheet(
    onDismiss: () -> Unit,
    onPaymentSelected: (PaymentMethod) -> Unit,
    onPayNowClicked: () -> Unit
) {
    var selectedPayment by remember { mutableStateOf<PaymentMethod?>(PaymentMethod.PAYPAL) }
    var email by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var mpesaNumber by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CC.primaryColor()
    ) {
        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Select Payment Method", style = CC.titleTextStyle(), fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PaymentOption(
                    name = "PayPal",
                    icon = painterResource(id = R.drawable.paypal),
                    isSelected = selectedPayment == PaymentMethod.PAYPAL,
                    onSelect = {
                        selectedPayment = PaymentMethod.PAYPAL
                        onPaymentSelected(PaymentMethod.PAYPAL)
                    }
                )
                PaymentOption(
                    name = "Credit Card",
                    icon = painterResource(id = R.drawable.creditcard),
                    isSelected = selectedPayment == PaymentMethod.CREDIT_CARD,
                    onSelect = {
                        selectedPayment = PaymentMethod.CREDIT_CARD
                        onPaymentSelected(PaymentMethod.CREDIT_CARD)
                    }
                )
                PaymentOption(
                    name = "M-Pesa",
                    icon = painterResource(id = R.drawable.mpesa),
                    isSelected = selectedPayment == PaymentMethod.MPESA,
                    onSelect = {
                        selectedPayment = PaymentMethod.MPESA
                        onPaymentSelected(PaymentMethod.MPESA)
                    }
                )
            }

            when (selectedPayment) {
                PaymentMethod.PAYPAL, PaymentMethod.CREDIT_CARD -> {
                    PaymentTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Email"
                    )
                    PaymentTextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Card Number"
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PaymentTextField(
                            value = expiryDate,
                            onValueChange = { expiryDate = it },
                            modifier = Modifier.weight(1f),
                            label = "Expiry Date"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        PaymentTextField(
                            value = cvv,
                            onValueChange = { cvv = it },
                            modifier = Modifier.weight(1f),
                            label = "CVV"
                        )
                    }
                }

                PaymentMethod.MPESA -> {
                    PaymentTextField(
                        value = mpesaNumber,
                        onValueChange = { mpesaNumber = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = "M-Pesa Number"
                    )
                }

                null -> {}
            }

            Button(
                onClick = onPayNowClicked,
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedPayment != null
            ) {
                Text("Pay Now", style = CC.bodyTextStyle())
            }
        }
    }
}

@Composable
fun PaymentOption(
    name: String,
    icon: Painter,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onSelect)
            .border(
                width = 2.dp,
                color = if (isSelected) CC.extraPrimaryColor() else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = name,
            modifier = Modifier.size(48.dp)
        )
        Text(name, style = CC.bodyTextStyle())
    }
}


enum class PaymentMethod {
    PAYPAL, CREDIT_CARD, MPESA
}


@Composable
fun PaymentTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        textStyle = CC.bodyTextStyle(),
        singleLine = true,
        onValueChange = onValueChange,
        label = { Text(label, style = CC.bodyTextStyle()) },
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CC.outLinedTextFieldColors()

    )
}


