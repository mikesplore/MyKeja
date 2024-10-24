package com.mike.hms.houses.bookHouse

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mike.hms.houses.bookHouse.housePayment.PaymentBottomSheet
import com.mike.hms.houses.bookHouse.housePayment.PaymentMethod
import com.mike.hms.houses.bookHouse.housePayment.ReceiptDialog
import com.mike.hms.model.getHouseViewModel
import com.mike.hms.model.getUserViewModel
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingInfoScreen(
    context: Context,
    houseID: String
) {
    val houseViewModel = getHouseViewModel(context)
    val userViewModel = getUserViewModel(context)
    val house by houseViewModel.house.observeAsState()
    val user  = UserEntity(
        userID = "",
        firstName = "Michael",
        lastName = "Odhiambo",
        gender = "Male",
        phoneNumber = "",
        role = "Tenant",
        email = "mikepremium8@gmail.com",
        photoUrl = ""
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val brush = Brush.horizontalGradient(
        listOf(CC.primaryColor(), CC.secondaryColor())
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    var showReceipt by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(PaymentMethod.PAYPAL) }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(houseID) {
        houseViewModel.getHouseByID(houseID)
    }

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
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text("Order Details", style = CC.titleTextStyle(), fontWeight = FontWeight.Bold)
                }
                // User Card
                UserDetails(user?: UserEntity())

                // House Details Card
                HouseDetailsCard(house ?: HouseEntity())

                // House Characteristics Card
                HouseCharacteristicsCard(house ?: HouseEntity())

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
            context = context,
            house = house ?: HouseEntity(),
            user = user?: UserEntity(),
            paymentMethod = selectedPaymentMethod,
            onDismiss = { showReceipt = false }
        )
    }
}







