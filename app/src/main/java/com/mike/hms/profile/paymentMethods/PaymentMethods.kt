package com.mike.hms.profile.paymentMethods

import android.content.Context
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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

    val paymentMethods = listOf(
        "Credit Card",
        "PayPal",
        "M-PESA"
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
                            color = if (selectedTab == index) CC.titleColor() else CC.textColor(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = if (selectedTab == index) CC.secondaryColor().copy(alpha = 0.5f)
                            else Color.Transparent,
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
                            color = if (selectedTab == index) CC.titleColor() else CC.textColor()
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
                    CreditCard(creditCard) {
                        creditCardViewModel.deleteCreditCard(creditCard.creditCard.userId)
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
                        payPalViewModel.deletePayPal(payPal.user.userID){success ->
                            if (success){
                                payPalViewModel.getPayPal(payPal.paypal.userId)
                                showAddPaymentMethod = !showAddPaymentMethod
                            }
                            else{
                                Toast.makeText(context, "Failed to delete PayPal. Try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    PaymentMethodEmptyState(
                        paymentType = "PayPal account",
                        showAddForm = showAddPaymentMethod,
                        onAddClick = { showAddPaymentMethod = !showAddPaymentMethod }
                    ) {
                        AddPayPalPayment(userEntity, payPalViewModel, context = context, onDismiss = {
                            showAddPaymentMethod = !showAddPaymentMethod
                        })
                    }
                }
            }

            2 -> { // M-PESA
                if (mpesa != null) {
                    SavedMpesaCard(mpesa) {
                        mpesaViewModel.deleteMpesa(mpesa.mpesa.userId){success ->
                            if (success){
                                mpesaViewModel.getMpesa(mpesa.mpesa.userId)
                                showAddPaymentMethod = !showAddPaymentMethod
                            }
                            else{
                                Toast.makeText(context, "Failed to delete M-PESA. Try again", Toast.LENGTH_SHORT).show()
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
                        AddMpesaPayment(userEntity = userEntity, mpesaViewModel = mpesaViewModel, context = context) {
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



