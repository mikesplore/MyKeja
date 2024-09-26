package com.mike.hms.houses.bookHouse

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.homeScreen.User
import com.mike.hms.homeScreen.houseTypes
import com.mike.hms.homeScreen.mockUser
import com.mike.hms.houses.bookHouse.housePayment.PaymentBottomSheet
import com.mike.hms.houses.bookHouse.housePayment.PaymentMethod
import com.mike.hms.houses.bookHouse.housePayment.ReceiptDialog
import com.mike.hms.houses.formatNumber
import com.mike.hms.model.houseModel.HouseEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingInfoScreen(
) {
    val house = houseTypes[0]
    val user = mockUser
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val brush = Brush.horizontalGradient(
        listOf(CC.primaryColor(), CC.secondaryColor())
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    var showReceipt by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    var email by remember { mutableStateOf("") }

    // Scaffold with a top app bar titled "Ticket"
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ticket") },
                colors = CC.topAppBarColors()
            )
        },
        containerColor = CC.primaryColor(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text("Order Details", style = CC.titleTextStyle(), fontWeight = FontWeight.Bold)
                }
                // User Card
                UserDetails(user)

                // House Details Card
                HouseDetailsCard(house)

                // House Characteristics Card
                HouseCharacteristicsCard(house)

                // Reschedule Card
                RescheduleCard()

                Spacer(modifier = Modifier.weight(1f))

                // Proceed to Checkout Button
                Button(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CC.extraPrimaryColor()
                    )
                ) {
                    Text(
                        text = "Proceed to Checkout", style = CC.bodyTextStyle().copy(
                            fontWeight = FontWeight.Bold,
                            color = CC.primaryColor()
                        )
                    )
                }
            }
        }
    )
    if (showBottomSheet) {
        PaymentBottomSheet(
            onDismiss = { showBottomSheet = false },
            onPaymentSelected = { paymentMethod ->
                selectedPaymentMethod = paymentMethod
            },
            onPayNowClicked = {
                showBottomSheet = false
                showReceipt = true
            }
        )
    }
    if (showReceipt) {
        ReceiptDialog(
            house = house,
            user = user,
            paymentMethod = selectedPaymentMethod,
            onDismiss = { showReceipt = false }
        )
    }
}







