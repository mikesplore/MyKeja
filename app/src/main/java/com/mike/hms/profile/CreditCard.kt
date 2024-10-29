package com.mike.hms.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.model.creditCardModel.CreditCardWithUser
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun CreditCard(
    creditCardWithUser: CreditCardWithUser
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                // Card Issuer Logo (placeholder)
                Text(
                    text = "VISA",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card Number
                Text(
                    text = creditCardWithUser.creditCard.cardNumber.chunked(4).joinToString(" "),
                    fontSize = 22.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Card Holder Name
                    Column {
                        Text(
                            text = "Card Holder",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${creditCardWithUser.user.firstName} ${creditCardWithUser.user.lastName}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Expiry Date
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Expires",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = creditCardWithUser.creditCard.expiryDate,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Edit and Delete buttons
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                IconButton(onClick = {
                    // Handle edit button click
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                }
                IconButton(onClick = {
                    // Handle delete button click
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun AddCreditCard() {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Add A Credit Card",
            style = CC.titleTextStyle(),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CC.MyOutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = "Card Number",
            modifier = Modifier.fillMaxWidth(),
            placeholder = "1234 5678 9012 3456"
        )

        Spacer(modifier = Modifier.height(8.dp))

        CC.MyOutlinedTextField(
            value = cardHolderName,
            onValueChange = { cardHolderName = it },
            label = "Card Holder Name",
            modifier = Modifier.fillMaxWidth(),
            placeholder = "John Doe"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CC.MyOutlinedTextField(
                value = expiryDate,
                onValueChange = { expiryDate = it },
                label =  "Expiry Date",
                modifier = Modifier.weight(1f),
                placeholder = "MM/YY"
            )

            Spacer(modifier = Modifier.width(8.dp))

            CC.MyOutlinedTextField(
                value = cvv,
                onValueChange = { cvv = it },
                label = "CVV",
                modifier = Modifier.weight(1f),
                placeholder = "123"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Here you would typically validate the input and create a CreditCardWithUser object

            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Card")
        }
    }
}