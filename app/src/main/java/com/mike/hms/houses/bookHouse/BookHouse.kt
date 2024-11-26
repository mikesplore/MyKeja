package com.mike.hms.houses.bookHouse

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.time.LocalDate
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mike.hms.HMSPreferences
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.paymentMethods.MpesaViewModel
import com.mike.hms.model.paymentMethods.PayPalViewModel
import com.mike.hms.model.userModel.UserViewModel
import java.time.format.DateTimeFormatter

@Composable
fun HouseBookingScreen(
    houseViewModel: HouseViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    creditCardViewModel: CreditCardViewModel = hiltViewModel(),
    paypalViewModel: PayPalViewModel = hiltViewModel(),
    mpesaViewModel: MpesaViewModel = hiltViewModel(),
    context: Context,
    houseId: String,
) {
    val scrollState = rememberScrollState()

    // States
    val house by houseViewModel.house.collectAsState()
    val user by userViewModel.user.collectAsState()
    val creditCard by creditCardViewModel.creditCard.collectAsState()
    val paypal by paypalViewModel.paypal.collectAsState()
    val mpesa by mpesaViewModel.mpesa.collectAsState()

    val savedPaymentMethods = listOfNotNull(
        creditCard,
        paypal,
        mpesa
    )

    var stayDuration by remember { mutableIntStateOf(1) }
    var checkInDate by remember { mutableStateOf<LocalDate?>(null) }
    val checkoutDate = checkInDate?.plusDays(stayDuration.toLong())
    var selectedPaymentMethod by remember { mutableStateOf<Any?>(null) }

    LaunchedEffect(Unit) {
        userViewModel.getUserByID(HMSPreferences.userId.value)
        mpesaViewModel.getMpesa(HMSPreferences.userId.value)
        paypalViewModel.getPayPal(HMSPreferences.userId.value)
        creditCardViewModel.getCreditCard(HMSPreferences.userId.value)
        houseViewModel.getHouseByID(houseId)
    }

    Column(
        modifier = Modifier
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
                        "About this place",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = house?.houseDescription ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Booking Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Booking Details",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Stay Duration with improved slider
                    Text(
                        "Length of Stay: $stayDuration days",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Slider(
                        value = stayDuration.toFloat(),
                        onValueChange = { stayDuration = it.toInt() },
                        valueRange = 1f..30f,
                        steps = 29,
                        modifier = Modifier.padding(vertical = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dates Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Check-in Date
                        Column(modifier = Modifier.weight(1f)) {
                            DatePickerButton(
                                label = "Check-in",
                                selectedDate = checkInDate,
                                onDateSelected = { checkInDate = it }
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        // Checkout Date Display
                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Check-out",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = checkoutDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                                            ?: "Select check-in first",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Payment Methods Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Payment Method",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (savedPaymentMethods.isNotEmpty()) {
                        savedPaymentMethods.forEach { paymentMethod ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedPaymentMethod = paymentMethod },
                                color = if (selectedPaymentMethod == paymentMethod)
                                    MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedPaymentMethod == paymentMethod)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedPaymentMethod == paymentMethod,
                                        onClick = { selectedPaymentMethod = paymentMethod },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        paymentMethod.toString(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No payment methods found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Payment Method")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Button
            Button(
                onClick = {
                    selectedPaymentMethod?.let { method ->
                        {
                            // Handle payment method selection
                        }
                    } ?: run {
                        Toast.makeText(context, "Please select a payment method.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedPaymentMethod != null && checkInDate != null,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Confirm Booking",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

//    if (showDatePicker) {
//        DatePickerDialog(
//            onDismissRequest = { showDatePicker = false },
//            confirmButton = {
//                Button(onClick = { showDatePicker = false }) {
//                    Text("Confirm")
//                }
//            },
//            dismissButton = {
//                Button(onClick = { showDatePicker = false }) {
//                    Text("Cancel")
//                }
//            },{}
//        )
//    }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showDatePicker = true }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = selectedDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                    ?: "Select date",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
