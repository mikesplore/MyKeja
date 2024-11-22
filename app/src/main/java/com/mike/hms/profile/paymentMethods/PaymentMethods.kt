package com.mike.hms.profile.paymentMethods

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.paymentMethods.CreditCardWithUser
import com.mike.hms.model.paymentMethods.MpesaViewModel
import com.mike.hms.model.paymentMethods.MpesaWithUser
import com.mike.hms.model.paymentMethods.PayPalViewModel
import com.mike.hms.model.paymentMethods.PayPalWithUser
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun PaymentMethodsSection(
    userEntity: UserEntity,
    creditCard: CreditCardWithUser?,
    payPal: PayPalWithUser?,
    mpesa: MpesaWithUser?,
    creditCardViewModel: CreditCardViewModel,
    payPalViewModel: PayPalViewModel,
    mpesaViewModel: MpesaViewModel,
    context: Context
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddPaymentMethod by remember { mutableStateOf(false) }
    var displayCvvDialog by remember { mutableStateOf(false) }

    val paymentMethods = listOf(
        "Credit Card",
        "PayPal",
        "M-PESA"
    )

    val brush = Brush.horizontalGradient(
        colors = listOf(CC.primaryColor(), CC.secondaryColor())
    )

    val transparentBrush = Brush.horizontalGradient(
        colors = listOf(Color.Transparent, Color.Transparent)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Payment Methods",
            style = CC.titleTextStyle(),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Method Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            paymentMethods.forEachIndexed { index, paymentMethod ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (selectedTab == index) CC.secondaryColor() else CC.textColor(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            brush = if (selectedTab == index) brush else transparentBrush,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .width(LocalConfiguration.current.screenWidthDp.dp * 0.28f)
                        .height(LocalConfiguration.current.screenWidthDp.dp * 0.1f)
                        .clickable { selectedTab = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        paymentMethod,
                        style = CC.contentTextStyle().copy(
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) CC.primaryColor() else CC.textColor()
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on selected tab
        when (selectedTab) {
            0 -> { // Credit Card
                if (creditCard != null) {
                    CreditCard(creditCard, showCvvDialog = displayCvvDialog, setShowCvvDialog = {
                        displayCvvDialog = false
                    }, onDelete = {
                        creditCardViewModel.deleteCreditCard(creditCard.creditCard.userId) { success ->
                            if (success) {
                                creditCardViewModel.getCreditCard(creditCard.creditCard.userId)
                                showAddPaymentMethod = !showAddPaymentMethod
                            }
                        }
                    })
                    HorizontalDivider(
                        color = CC.textColor().copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Display card details", style = CC.contentTextStyle())
                        Spacer(modifier = Modifier.weight(1f))
                        IconToggleButton(
                            onCheckedChange = { displayCvvDialog = !displayCvvDialog },
                            checked = displayCvvDialog
                        ) {
                            Icon(
                                if (displayCvvDialog) Icons.Default.LockOpen else Icons.Default.Lock,
                                contentDescription = "Display card details",
                                tint = CC.textColor()
                            )
                        }
                    }
                } else {
                    PaymentMethodEmptyState(
                        paymentType = "credit card",
                        showAddForm = showAddPaymentMethod,
                        onAddClick = { showAddPaymentMethod = !showAddPaymentMethod }
                    ) {
                        AddCreditCard(context, creditCardViewModel) { show ->
                            showAddPaymentMethod = show
                        }
                    }
                }
            }

            1 -> { // PayPal
                if (payPal != null) {
                    SavedPayPalCard(payPal) {
                        payPalViewModel.deletePayPal(payPal.user.userID) { success ->
                            if (success) {
                                payPalViewModel.getPayPal(payPal.paypal.userId)
                                showAddPaymentMethod = !showAddPaymentMethod
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to delete PayPal. Try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    PaymentMethodEmptyState(
                        paymentType = "PayPal account",
                        showAddForm = showAddPaymentMethod,
                        onAddClick = { showAddPaymentMethod = !showAddPaymentMethod }
                    ) {
                        AddPayPalPayment(
                            userEntity,
                            payPalViewModel,
                            context = context,
                            onDismiss = {
                                showAddPaymentMethod = !showAddPaymentMethod
                            })
                    }
                }
            }

            2 -> { // M-PESA
                if (mpesa != null) {
                    SavedMpesaCard(mpesa) {
                        mpesaViewModel.deleteMpesa(mpesa.mpesa.userId) { success ->
                            if (success) {
                                mpesaViewModel.getMpesa(mpesa.mpesa.userId)
                                showAddPaymentMethod = !showAddPaymentMethod
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to delete M-PESA. Try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                } else {
                    PaymentMethodEmptyState(
                        paymentType = "M-PESA number",
                        showAddForm = showAddPaymentMethod,
                        onAddClick = { showAddPaymentMethod = !showAddPaymentMethod }
                    ) {
                        // Add M-PESA form here
                        AddMpesaPayment(
                            userEntity = userEntity,
                            mpesaViewModel = mpesaViewModel,
                            context = context
                        ) {
                            showAddPaymentMethod = !showAddPaymentMethod
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun PaymentMethodEmptyState(
    paymentType: String,
    showAddForm: Boolean,
    onAddClick: () -> Unit,
    addForm: @Composable () -> Unit
) {
    Column {
        Row {
            Text(
                "You don't have a saved $paymentType yet. ",
                style = CC.contentTextStyle()
            )
            Text(
                "Add one",
                style = CC.contentTextStyle().copy(color = CC.tertiaryColor()),
                modifier = Modifier.clickable(onClick = onAddClick)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        AnimatedVisibility(showAddForm) {
            addForm()
        }
    }
}



