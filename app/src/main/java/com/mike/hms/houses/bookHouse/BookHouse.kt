package com.mike.hms.houses.bookHouse

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.KingBed
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mike.hms.HMSPreferences
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.paymentMethods.CreditCardEntity
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.paymentMethods.MpesaViewModel
import com.mike.hms.model.paymentMethods.PayPalViewModel
import com.mike.hms.model.transactions.PaymentMethod
import com.mike.hms.model.transactions.TransactionEntity
import com.mike.hms.model.transactions.TransactionType
import com.mike.hms.model.transactions.TransactionViewModel
import com.mike.hms.model.userModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun HouseBookingScreen(
    houseViewModel: HouseViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    creditCardViewModel: CreditCardViewModel = hiltViewModel(),
    paypalViewModel: PayPalViewModel = hiltViewModel(),
    mpesaViewModel: MpesaViewModel = hiltViewModel(),
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    context: Context,
    houseId: String,
) {
    val scrollState = rememberScrollState()

    // States
    val house by houseViewModel.house.collectAsState()
    val creditCard by creditCardViewModel.creditCard.collectAsState()
    val paypal by paypalViewModel.paypal.collectAsState()
    val mpesa by mpesaViewModel.mpesa.collectAsState()

    listOfNotNull(
        creditCard,
        paypal,
        mpesa
    )

    var stayDuration by remember { mutableIntStateOf(1) }
    var checkInDate by remember { mutableStateOf<LocalDate?>(null) }
    val checkoutDate = checkInDate?.plusDays(stayDuration.toLong())

    LaunchedEffect(Unit) {
        userViewModel.getUserByID(HMSPreferences.userId.value)
        mpesaViewModel.getMpesa(HMSPreferences.userId.value)
        paypalViewModel.getPayPal(HMSPreferences.userId.value)
        creditCardViewModel.getCreditCard(HMSPreferences.userId.value)
        houseViewModel.getHouseByID(houseId)
    }

    Column(
        modifier = Modifier
            .background(CC.primaryColor())
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Hero Section with Image and Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = house?.houseImageLink?.firstOrNull(),
                contentDescription = "House Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 100f
                        )
                    )
            )
            // House title and basic info overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = house?.houseName ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Text(
                    text = "${house?.houseLocation} • ${house?.houseType?.name} • ${house?.houseRating} ⭐",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Content Section
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Description Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "What to Know",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(
                            icon = Icons.Filled.Person,
                            text = "${house?.houseCapacity} Guests"
                        )
                        InfoItem(
                            icon = Icons.Filled.AttachMoney,
                            text = "Ksh ${house?.housePrice} per night"
                        )
                        InfoItem(
                            icon = Icons.Filled.KingBed,
                            text = "${house?.numberOfRooms} Rooms"
                        )
                    }
                }
            }

            // Booking Details Card
            BookingDetailsCard(
                stayDuration = stayDuration,
                onStayDurationChange = { stayDuration = it },
                checkInDate = checkInDate,
                onCheckInDateSelected = { checkInDate = it },
                checkoutDate = checkoutDate,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Payment Methods Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                val amountPaid = house?.housePrice?.toDouble()?.times(stayDuration.toDouble())
                PaymentSection(
                    amountToPay = amountPaid ?: 0.0,
                    creditCardBalance = creditCard?.creditCard?.balance?.toDouble(),
                    mpesa = null,
                    paypal = null,
                    onPaymentComplete = {
                        CC.generateTransactionId { id ->
                            val transaction = TransactionEntity(
                                transactionID = id,
                                paymentMethod = PaymentMethod.CREDIT_CARD,
                                transactionType = TransactionType.SUBTRACTION,
                                amount = (house?.housePrice?.toDouble()
                                    ?.times(stayDuration.toDouble()))?.toInt().toString(),
                                userId = creditCard?.creditCard?.userId.toString(),
                                date = CC.getCurrentDate()
                            )

                            transactionViewModel.addTransaction(transaction) { success ->
                                if (success) {
                                    val newCreditCardBalance =
                                        creditCard?.creditCard?.balance?.toDouble()
                                            ?.minus(amountPaid ?: 0.0)
                                    val creditCard = CreditCardEntity(
                                        userId = creditCard?.creditCard?.userId.toString(),
                                        cardNumber = creditCard?.creditCard?.cardNumber.toString(),
                                        balance = newCreditCardBalance?.toInt().toString(),
                                        expiryDate = creditCard?.creditCard?.expiryDate.toString(),
                                        cvv = creditCard?.creditCard?.cvv.toString(),
                                        cardId = creditCard?.creditCard?.cardId.toString()
                                    )
                                    creditCardViewModel.insertCreditCard(creditCard) {
                                        creditCardViewModel.getCreditCard(creditCard.userId.toString())
                                    }


                                } else {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    },
                    context
                )

            }

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun BookingDetailsCard(
    stayDuration: Int,
    onStayDurationChange: (Int) -> Unit,
    checkInDate: LocalDate?,
    onCheckInDateSelected: (LocalDate) -> Unit,
    checkoutDate: LocalDate?,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        CC.DatePickerLocalDate(
            onDateSelected = onCheckInDateSelected,
            onShowDatePickerChange = { showDatePicker = it })
    }
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(CC.primaryColor().copy(0.5f))
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Booking Details",
                    style = CC.titleTextStyle()
                )
                Icon(
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = "Calendar",
                    tint = CC.secondaryColor()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stay Duration Section
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Length of Stay",
                        style = CC.contentTextStyle()
                    )
                    Text(
                        "$stayDuration days",
                        style = CC.contentTextStyle()
                    )
                }

                Slider(
                    value = stayDuration.toFloat(),
                    onValueChange = { onStayDurationChange(it.toInt()) },
                    valueRange = 1f..30f,
                    steps = 29,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = CC.secondaryColor(),
                        activeTrackColor = CC.titleColor(),
                        inactiveTrackColor = CC.titleColor().copy(alpha = 0.2f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Dates Section
            Column(
                modifier = Modifier

                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Check-in Date
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .background(CC.secondaryColor().copy(0.5f))
                            .fillMaxWidth()
                            .clickable {
                                showDatePicker = true
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Check-in",
                                style = CC.contentTextStyle()
                                    .copy(color = CC.primaryColor(), fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                checkInDate?.format(DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy"))
                                    ?: "Select date",
                                style = CC.contentTextStyle()
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = "Select date",
                            tint = CC.primaryColor()
                        )
                    }
                }

                // Check-out Date
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .background(CC.secondaryColor().copy(0.5f))
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Check-out",
                                style = CC.contentTextStyle()
                                    .copy(color = CC.primaryColor(), fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                checkoutDate?.format(DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy"))
                                    ?: "Select check-in first",
                                style = CC.contentTextStyle()
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.EventAvailable,
                            contentDescription = "Checkout date",
                            tint = CC.primaryColor()
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PaymentSection(
    amountToPay: Double,
    creditCardBalance: Double?,
    mpesa: String?,
    paypal: String?,
    onPaymentComplete: (Boolean) -> Unit,
    context: Context
) {
    var selectedMethod by remember { mutableStateOf<String?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Amount Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Amount to Pay",
                    style = CC.titleTextStyle()
                )
                Text(
                    "$amountToPay",
                    style = CC.titleTextStyle(),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            "Available Payment Methods",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Credit Card Option
        if (creditCardBalance != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(enabled = creditCardBalance >= amountToPay) {
                        selectedMethod = "Credit Card"
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedMethod == "Credit Card")
                        MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    if (selectedMethod == "Credit Card")
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Credit Card",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            if (creditCardBalance >= amountToPay)
                                "Available Balance: $${String.format("%.2f", creditCardBalance)}"
                            else
                                "Insufficient Balance",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (creditCardBalance >= amountToPay)
                                MaterialTheme.colorScheme.outline
                            else MaterialTheme.colorScheme.error
                        )
                    }
                    if (creditCardBalance >= amountToPay) {
                        RadioButton(
                            selected = selectedMethod == "Credit Card",
                            onClick = { selectedMethod = "Credit Card" }
                        )
                    }
                }
            }
        }

        // M-Pesa Option
        if (mpesa != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "M-Pesa",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Coming soon",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        // PayPal Option
        if (paypal != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "PayPal",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Coming soon",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pay Button
        Button(
            onClick = { showConfirmDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = selectedMethod != null && !isProcessing
        ) {
            if (isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    "Complete Payment",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    // Confirmation Dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Payment") },
            text = {
                Text(
                    "Would you like to complete the payment of $${
                        String.format(
                            "%.2f",
                            amountToPay
                        )
                    }?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        isProcessing = true
                        // Simulate payment process
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1500)
                            isProcessing = false
                            val success = when (selectedMethod) {
                                "Credit Card" -> creditCardBalance != null && creditCardBalance >= amountToPay
                                else -> false
                            }
                            onPaymentComplete(success)
                            Toast.makeText(
                                context,
                                if (success) "Payment successful!" else "Payment failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
