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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.houses.formatNumber
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.paymentMethods.CreditCardWithUser
import com.mike.hms.model.paymentMethods.MpesaViewModel
import com.mike.hms.model.paymentMethods.MpesaWithUser
import com.mike.hms.model.paymentMethods.PayPalViewModel
import com.mike.hms.model.paymentMethods.PayPalWithUser
import com.mike.hms.model.transactions.PaymentMethod
import com.mike.hms.model.transactions.TransactionEntity
import com.mike.hms.model.transactions.TransactionType
import com.mike.hms.model.transactions.TransactionViewModel
import com.mike.hms.model.userModel.UserEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun PaymentMethodsSection(
    userEntity: UserEntity,
    creditCard: CreditCardWithUser?,
    payPal: PayPalWithUser?,
    mpesa: MpesaWithUser?,
    creditCardViewModel: CreditCardViewModel,
    transactionViewModel: TransactionViewModel,
    payPalViewModel: PayPalViewModel,
    mpesaViewModel: MpesaViewModel,
    context: Context
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddPaymentMethod by remember { mutableStateOf(false) }
    var displayCvvDialog by remember { mutableStateOf(false) }
    var displayCardBalance by remember { mutableStateOf(false) }
    var displayAddFundsDialog by remember { mutableStateOf(false) }

    if (displayAddFundsDialog) {
        AddFundsDialog(
            creditCard = creditCard,
            creditCardViewModel = creditCardViewModel,
            transactionViewModel = transactionViewModel,
            onDismiss = { displayAddFundsDialog = false }
        )
    }

    val balance = creditCard?.creditCard?.balance?.toString().orEmpty().ifEmpty { "0" }

    if (displayCardBalance) {
        CC.ConfirmDialog(
            title = "Card Balance",
            message = "Your card balance is Ksh. ${formatNumber(balance.toInt())}",
            confirmText = "Dismiss",
            onConfirm = {
                displayCardBalance = false
            },
            onDismiss = { displayCardBalance = false }
        )
    }
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
                //Payment method Box
                PaymentMethodBox(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    index = index,
                    paymentMethod = paymentMethod,
                    brush = brush,
                    transparentBrush = transparentBrush
                )
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

                    //Card Sub Menu
                    Spacer(modifier = Modifier.height(10.dp))
                    CardSubMenu(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        displayCvvDialog = { displayCvvDialog = !displayCvvDialog },
                        text = "Display Card Details",
                        icon = Icons.Filled.RemoveRedEye
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    CardSubMenu(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        displayCvvDialog = { displayCardBalance = !displayCardBalance },
                        text = "Display Card Balance",
                        icon = Icons.Outlined.Money
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CardSubMenu(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        displayCvvDialog = { displayAddFundsDialog = !displayAddFundsDialog },
                        text = "Add Funds",
                        icon = Icons.Filled.AttachMoney
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(
                        color = CC.textColor().copy(alpha = 0.2f),
                        thickness = 1.dp
                    )

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
fun PaymentMethodBox(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    index: Int,
    paymentMethod: String,
    brush: Brush,
    transparentBrush: Brush
) {
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
            .clickable { onTabSelected(index) },
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

@Composable
fun CardSubMenu(
    displayCvvDialog: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CC.extraSecondaryColor())
            .clickable { displayCvvDialog() }
            .fillMaxWidth(0.9f)
            .padding(vertical = 12.dp), // Increased padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CC.textColor(),
            modifier = Modifier
                .padding(start = 8.dp) // Increased padding
                .size(24.dp) // Increased icon size
        )
        Spacer(modifier = Modifier.width(16.dp)) // Added spacer between icon and text
        Text(
            text = text,
            style = CC.contentTextStyle().copy(
                fontWeight = FontWeight.Medium, // Added font weight
                fontSize = 16.sp // Increased font size
            )
        )
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


@Composable
fun AddFundsDialog(
    creditCard: CreditCardWithUser?, // Pass the CreditCardWithUser object
    creditCardViewModel: CreditCardViewModel, // ViewModel for handling logic
    transactionViewModel: TransactionViewModel,
    onDismiss: () -> Unit
) {
        var amount by remember { mutableStateOf("") }
        var successMessage by remember { mutableStateOf("Enter the amount you want to add.") }
        var isError by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        CC.ConfirmDialog(
            title = "Add Funds",
            message = successMessage,
            textFieldValue = amount,
            keyboardType = KeyboardType.Phone,
            onTextFieldValueChange = { amount = it }, // Update the amount correctly
            confirmText = "Add Funds",
            messageColor = if (isError) Color.Red else CC.textColor(),
            onConfirm = {
                when {
                    amount.isEmpty() -> {
                        successMessage = "Please enter the amount you want to add to your account"
                        return@ConfirmDialog
                    }
                    amount.any { !it.isDigit() } -> {
                        successMessage = "Invalid input. Please enter a valid numeric amount."
                        isError = true
                        return@ConfirmDialog
                    }
                    else -> {
                        val currentBalance = creditCard?.creditCard?.balance?.toIntOrNull() ?: 0 // Convert current balance to Int, default to 0
                        val enteredAmount = amount.toIntOrNull() // Convert the entered amount to Int

                        if (enteredAmount == null) {
                            successMessage = "Invalid input. Please enter a valid numeric amount."
                            isError = true
                        } else {
                            val newBalance = currentBalance + enteredAmount // Add the current balance and entered amount
                            val updatedCreditCard = creditCard?.creditCard?.copy(balance = newBalance.toString()) // Convert the result back to String

                            if (updatedCreditCard != null) {
                                creditCardViewModel.insertCreditCard(updatedCreditCard) { success ->
                                    if (success) {
                                        CC.generateTransactionId { id ->
                                        val transaction = TransactionEntity(
                                          transactionID = id,
                                            paymentMethod = PaymentMethod.CREDIT_CARD,
                                            transactionType = TransactionType.ADDITION,
                                            amount = amount,
                                            userId = creditCard.creditCard.userId,
                                            date = CC.getCurrentDate()
                                        )

                                        transactionViewModel.insertTransaction(transaction){ success ->
                                            if(success){
                                                successMessage = "Funds added successfully!"
                                                creditCardViewModel.getCreditCard(creditCard.creditCard.userId)
                                                amount = ""
                                            }
                                            else {
                                                successMessage = "An Error Occurred"
                                                isError = true
                                            }
                                        }}

                                        scope.launch {
                                            delay(1000)
                                            onDismiss() // Close dialog after success
                                        }
                                    } else {
                                        successMessage = "Failed to add funds. Try again."
                                        isError = true
                                    }
                                }
                            } else {
                                successMessage = "Failed to process the request. Try again."
                                isError = true
                            }
                        }
                    }
                }
            },
            onDismiss = onDismiss
        )

}



