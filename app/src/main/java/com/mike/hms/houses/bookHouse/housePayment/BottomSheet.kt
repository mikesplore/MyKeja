package com.mike.hms.houses.bookHouse.housePayment

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.window.Dialog
import com.mike.hms.R
import com.mike.hms.homeScreen.User
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.ui.theme.CommonComponents
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentBottomSheet(
    onDismiss: () -> Unit,
    onPaymentSelected: (PaymentMethod) -> Unit,
    onPayNowClicked: () -> Unit
) {
    var selectedPayment by remember { mutableStateOf<PaymentMethod?>(null) }
    var email by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var mpesaNumber by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CommonComponents.primaryColor()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Select Payment Method", style = CommonComponents.titleTextStyle(), fontWeight = FontWeight.Bold)

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
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        label = { Text("Card Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = expiryDate,
                            onValueChange = { expiryDate = it },
                            label = { Text("Expiry Date") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = cvv,
                            onValueChange = { cvv = it },
                            label = { Text("CVV") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                PaymentMethod.MPESA -> {
                    OutlinedTextField(
                        value = mpesaNumber,
                        onValueChange = { mpesaNumber = it },
                        label = { Text("M-Pesa Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                null -> {}
            }

            Button(
                onClick = onPayNowClicked,
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedPayment != null
            ) {
                Text("Pay Now")
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
                color = if (isSelected) CommonComponents.extraPrimaryColor() else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = name,
            modifier = Modifier.size(48.dp)
        )
        Text(name, style = CommonComponents.bodyTextStyle())
    }
}

@Composable
fun ReceiptDialog(
    house: HouseEntity,
    user: User,
    paymentMethod: PaymentMethod?,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Purchase Receipt", style = CommonComponents.titleTextStyle(), fontWeight = FontWeight.Bold)
                HorizontalDivider()
                Text("House: ${house.houseName}", style = CommonComponents.bodyTextStyle())
                Text("Price: $${house.housePrice}", style = CommonComponents.bodyTextStyle())
                Text("Payment Method: ${paymentMethod?.name ?: "N/A"}", style = CommonComponents.bodyTextStyle())
                Text("Buyer: ${user.fullName}", style = CommonComponents.bodyTextStyle())
                Text("Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}", style = CommonComponents.bodyTextStyle())
                HorizontalDivider()
                Text("Thank you for your purchase! Enjoy your time in the house.", style = CommonComponents.bodyTextStyle(), fontWeight = FontWeight.Bold)
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

enum class PaymentMethod {
    PAYPAL, CREDIT_CARD, MPESA
}
