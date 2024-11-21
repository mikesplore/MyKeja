package com.mike.hms.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mike.hms.HMSPreferences
import com.mike.hms.model.creditCardModel.CreditCardEntity
import com.mike.hms.model.creditCardModel.CreditCardViewModel
import com.mike.hms.model.creditCardModel.CreditCardWithUser
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun CreditCard(
    creditCardWithUser: CreditCardWithUser,
    onDelete: () -> Unit,

    ) {
    val cardImageUrl = listOf(
        "https://img.freepik.com/free-photo/view-wild-lion-nature_23-2150460851.jpg",
        "https://media.wired.com/photos/65caa6f6f553745750c04769/master/w_2560%2Cc_limit/elephant-congo-science-GettyImages-630005418.jpg",
        "https://files.worldwildlife.org/wwfcmsprod/images/White_Rhino/hero_small/3yuabfu3jq_white_rhino_42993643.jpg",
        "https://d1jyxxz9imt9yb.cloudfront.net/animal/234/meta_image/regular/LC202303_AmboseliCommsSummit_082_404092_reduced.jpg"
    )

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
        ) {
            // Delete Button
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Card",
                    tint = Color.White
                )
            }

            // Card Image or Placeholder
            AsyncImage(
                model = cardImageUrl.random(),
                contentDescription = "Card Image",
                modifier = Modifier
                    .fillMaxSize()  // Make the image cover the entire card
                    .align(Alignment.Center),  // Center the image
                contentScale = ContentScale.Crop  // Crop the image to cover the card area
            )

            // Overlay with text and details
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Add padding to ensure text doesn't overlap the edges
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Card Issuer Logo/Text
                Text(
                    text = "VISA",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.align(Alignment.End)
                )

                // Card Number
                Text(
                    text = creditCardWithUser.creditCard.cardNumber
                        .chunked(4)
                        .joinToString(" "),
                    fontSize = 22.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    style = TextStyle( // Apply bold and shadow
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Black, // Shadow color
                            offset = Offset(2f, 2f), // Shadow offset
                            blurRadius = 4f // Shadow blur radius
                        )
                    )
                )

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
        }
    }
}


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