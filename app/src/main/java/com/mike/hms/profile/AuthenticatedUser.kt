package com.mike.hms.profile

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.HMSPreferences
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.paymentMethods.MpesaViewModel
import com.mike.hms.model.paymentMethods.PayPalViewModel
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.profile.paymentMethods.PaymentMethodsSection
import kotlinx.coroutines.delay
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun AuthenticatedUser(
    navController: NavController,
    context: Context = LocalContext.current
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val creditCardViewModel: CreditCardViewModel = hiltViewModel()
    val mpesaViewModel: MpesaViewModel = hiltViewModel()
    val payPalViewModel: PayPalViewModel = hiltViewModel()

    val email = FirebaseAuth.getInstance().currentUser?.email
    val user by userViewModel.user.collectAsState()
    val creditCard by creditCardViewModel.creditCard.collectAsState()
    val mpesa by mpesaViewModel.mpesa.collectAsState()
    val payPal by payPalViewModel.payPal.collectAsState()

    listOf(
        "Credit Card",
        "PayPal",
        "M-PESA"
    )

    remember { mutableStateOf<String?>("Credit Card") }
    LaunchedEffect(HMSPreferences.userId.value) {
        val maxRetries = 3
        var attempt = 0

        while (attempt < maxRetries) {
            // Fetch the user by email
            userViewModel.getUserByEmail(email!!)

            // Fetch the M-PESA and PayPal entities
            mpesaViewModel.getMpesa(HMSPreferences.userId.value)
            payPalViewModel.getPayPal(HMSPreferences.userId.value)

            Log.d(
                "CreditCardViewModell",
                "Attempt ${attempt + 1}: Fetching credit card for user ID: ${HMSPreferences.userId.value}"
            )

            // Fetch the credit card
            creditCardViewModel.getCreditCard(HMSPreferences.userId.value)

            // Check if the credit card is fetched (assuming creditCard is a state or flow)

            if (creditCard != null) {
                Log.d("CreditCardViewModell", "Credit card fetched successfully: $creditCard")
                break
            }

            attempt++
            delay(1000L) // Wait for 1 second before retrying
        }

        if (creditCard == null) {
            Log.d("CreditCardViewModell", "Failed to fetch credit card after $maxRetries attempts.")
        }
    }




    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxSize()
            .padding(16.dp)
    ) {
        user?.let { UserCard(it) }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            color = CC.textColor(),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        RowTopic(
            icon = Icons.Default.ManageAccounts,
            text = "Manage Account",
            navController = navController,
            destination = "manageAccount"
        )
        Spacer(modifier = Modifier.height(10.dp))
        RowTopic(
            icon = Icons.Default.CreditCard,
            text = "Transaction History",
            navController = navController,
            destination = ""
        )
        Spacer(modifier = Modifier.height(10.dp))
        RowTopic(
            icon = Icons.Default.CollectionsBookmark,
            text = "Bookings History",
            navController = navController,
            destination = ""
        )
        Spacer(modifier = Modifier.height(10.dp))
        RowTopic(
            icon = Icons.Default.RateReview,
            text = "Ratings and Reviews",
            navController = navController,
            destination = ""
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            color = CC.textColor(),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        // Payment Methods Section
        user?.let { user ->
            PaymentMethodsSection(
                userEntity = user,
                creditCard = creditCard,
                payPal = payPal,
                mpesa = mpesa,
                creditCardViewModel = creditCardViewModel,
                payPalViewModel = payPalViewModel,
                mpesaViewModel = mpesaViewModel,
                context = context
            )
        }
    }

}


@Composable
fun RowTopic(icon: ImageVector, text: String, navController: NavController, destination: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp


    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = CC.textColor().copy(0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .height(screenWidth * 0.15f)
            .width(screenWidth * 0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier
                .padding(start = 10.dp)
                .size(screenWidth * 0.1f),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = CC.tertiaryColor()
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CC.textColor(),
                modifier = Modifier.size(screenWidth * 0.05f)

            )
        }

        Text(text, style = CC.contentTextStyle())

        IconButton(onClick = {
            if (destination.isNotEmpty()) {
                navController.navigate(destination)
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                contentDescription = null,
                tint = CC.textColor(),
                modifier = Modifier.size(screenWidth * 0.05f)
            )
        }

    }
}

