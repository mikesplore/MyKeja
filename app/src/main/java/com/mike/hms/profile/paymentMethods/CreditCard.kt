package com.mike.hms.profile.paymentMethods

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mike.hms.model.paymentMethods.CreditCardWithUser
import kotlin.math.abs
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun CreditCard(
    creditCardWithUser: CreditCardWithUser,
    showCvvDialog: Boolean = false,
    setShowCvvDialog: (Boolean) -> Unit = {},
    onDelete: () -> Unit

) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var isDetailsVisible by remember { mutableStateOf(false) }
    var enteredCvv by remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }

    var message by remember { mutableStateOf("Please enter the CVV for the credit card.") }
    val cardImageUrl = listOf(
        "https://img.freepik.com/free-photo/view-wild-lion-nature_23-2150460851.jpg",
        "https://ajkenyasafaris.com/wp-content/uploads/2023/10/image1-1024x683.png.webp",
        "https://upload.wikimedia.org/wikipedia/commons/0/0a/Standing_jaguar.jpg",
        "https://d1jyxxz9imt9yb.cloudfront.net/animal/234/meta_image/regular/LC202303_AmboseliCommsSummit_082_404092_reduced.jpg",
        "https://t4.ftcdn.net/jpg/09/58/40/81/360_F_958408177_JxtISGf1k7ZUM2mRnC8KAbt8RdYYBUbi.jpg",
        "https://t3.ftcdn.net/jpg/08/56/23/24/360_F_856232449_2SAIyzHtoTBEGnK7lPm9KZdIbUrTeBuq.jpg"
    )
    val cardRotation = remember { Animatable(0f) }

    if (showConfirmDialog) {
        CC.ConfirmDialog(
            title = "Delete Credit Card",
            message = "Are you sure you want to delete this credit card?",
            messageColor = CC.textColor(),
            onConfirm = {
                onDelete()
                showConfirmDialog = false
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }

    if (showCvvDialog) {
        CC.ConfirmDialog(
            title = "Enter CVV",
            message = message,
            messageColor = if (isError.value) Color.Red else CC.textColor(),
            textFieldValue = enteredCvv,
            onTextFieldValueChange = { enteredCvv = it },
            keyboardType = KeyboardType.Phone,
            onDismiss = {
                setShowCvvDialog(false)
                isError.value = false
                enteredCvv = ""
                message = "Please enter the CVV for the credit card."
            },
            onConfirm = {
                if (enteredCvv == creditCardWithUser.creditCard.cvv) {
                    isDetailsVisible = true
                    setShowCvvDialog(true)
                    enteredCvv = ""
                } else {
                    isError.value = true
                    message = "Incorrect CVV. Please try again."
                    enteredCvv = ""
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .graphicsLayer(rotationY = cardRotation.value),
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
            AsyncImage(
                model = cardImageUrl[findDigitAndTransform(creditCardWithUser.creditCard.cardNumber)],
                contentDescription = "Card Image",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = {
                    showConfirmDialog = true
                },
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "VISA",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.align(Alignment.End)
                )

                Text(
                    text = if (isDetailsVisible) {
                        creditCardWithUser.creditCard.cardNumber
                            .chunked(4)
                            .joinToString(" ")
                    } else {
                        "${
                            creditCardWithUser.creditCard.cardNumber.chunked(2).first()
                        } ** **** **** ** ${creditCardWithUser.creditCard.cardNumber.chunked(2).last()}"
                    },
                    fontSize = 22.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Card Holder",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "${creditCardWithUser.user.firstName.uppercase()} ${creditCardWithUser.user.lastName.uppercase()}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Expires",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
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

/**
 * This function takes a credit card number as input and returns the absolute difference between the most frequent digit and 6.
 * @param cardNumber: the credit card number as a string.
 *@return: the absolute difference between the most frequent digit and 6.
 */

fun findDigitAndTransform(cardNumber: String): Int {
    val digitFrequencies = mutableMapOf<Char, Int>()

    for (digit in cardNumber) {
        if (digit.isDigit()) {
            digitFrequencies[digit] = digitFrequencies.getOrDefault(digit, 0) + 1
        }
    }

    val mostFrequentDigit = digitFrequencies.maxByOrNull { it.value }?.key

    return if (mostFrequentDigit != null && mostFrequentDigit.isDigit()) {
        val digit = mostFrequentDigit.digitToInt()
        val transformedValue = abs(6 - digit)
        transformedValue.coerceIn(0, 5) // Ensure result is between 0 and 5
    } else {
        0 // Default value if no digits or most frequent digit is not a number
    }
}