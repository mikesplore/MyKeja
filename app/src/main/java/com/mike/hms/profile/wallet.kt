package com.mike.hms.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mike.hms.HMSPreferences
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.paymentMethods.MpesaViewModel
import com.mike.hms.model.paymentMethods.PayPalViewModel
import com.mike.hms.model.transactions.TransactionViewModel
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.profile.paymentMethods.PaymentMethodsSection
import java.text.NumberFormat
import java.util.*
import com.mike.hms.ui.theme.CommonComponents as CC

data class Transaction(
    val id: Int,
    val type: TransactionType,
    val amount: Double,
    val recipient: String,
    val date: String
)

enum class TransactionType {
    TRANSFER, WITHDRAWAL, TIP, DEPOSIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(context: Context, navController: NavController) {
    var showBalance by remember { mutableStateOf(true) }
    val balance = remember { 2547.89 }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    val textSize = with(density) { (screenWidth * 0.05f).toSp() }

    val userViewModel: UserViewModel = hiltViewModel()
    val creditCardViewModel: CreditCardViewModel = hiltViewModel()
    val mpesaViewModel: MpesaViewModel = hiltViewModel()
    val payPalViewModel: PayPalViewModel = hiltViewModel()
    val transactionViewModel: TransactionViewModel = hiltViewModel()


    val user by userViewModel.user.collectAsState()
    val creditCard by creditCardViewModel.creditCard.collectAsState()
    val mpesa by mpesaViewModel.mpesa.collectAsState()
    val payPal by payPalViewModel.paypal.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getUserByID(HMSPreferences.userId.value)
        creditCardViewModel.getCreditCard(HMSPreferences.userId.value)
        mpesaViewModel.getMpesa(HMSPreferences.userId.value)
        payPalViewModel.getPayPal(HMSPreferences.userId.value)
    }

    // Mock transactions
    val transactions = remember {
        listOf(
            Transaction(1, TransactionType.TRANSFER, -150.00, "John Doe", "2024-11-28"),
            Transaction(2, TransactionType.TIP, -5.00, "Coffee Shop", "2024-11-28"),
            Transaction(3, TransactionType.WITHDRAWAL, -200.00, "ATM", "2024-11-27"),
            Transaction(4, TransactionType.DEPOSIT, 1000.00, "Salary", "2024-11-26"),
            Transaction(5, TransactionType.TRANSFER, -80.00, "Jane Smith", "2024-11-25")
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Wallet", style = CC.titleTextStyle(), modifier = Modifier.clickable{ HMSPreferences.saveDarkModePreference(isDarkMode = !HMSPreferences.darkMode.value)})
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBackIos, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CC.primaryColor()
                )
            )
        },
        containerColor = CC.primaryColor()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Balance Card
            BalanceCard(
                balance = balance,
                showBalance = showBalance,
                showBalanceChange = { showBalance = it },
                textSize = textSize
            )

            Column {  }
            Spacer(modifier = Modifier.height(16.dp))

            // Wallet Operations
            WalletOperations()

            Spacer(modifier = Modifier.height(16.dp))

            // Recent Transactions
            Row (
                modifier = Modifier
                    .height(screenWidth * 0.1f)
                    .fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Recent Transactions", style = CC.titleTextStyle())
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {navController.navigate("transaction")}) {
                    Text("View all", style = CC.contentTextStyle())
                }
            }
                Column(
                    modifier = Modifier.heightIn(min = screenHeight * 0.1f)
                ) {
                    if (transactions.isEmpty()) {
                        Text("No transactions found", style = CC.contentTextStyle())
                    }
                    transactions.forEach { transaction ->
                        TransactionItem(transaction = transaction)
                    }
           }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .height(screenWidth * 0.1f)
                    .fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Text("Payment Methods", style = CC.titleTextStyle())
            }
            PaymentMethodsSection(
                userEntity = user?: UserEntity(),
                creditCard = creditCard,
                payPal = payPal,
                mpesa = mpesa,
                creditCardViewModel = creditCardViewModel,
                transactionViewModel = transactionViewModel,
                payPalViewModel = payPalViewModel,
                mpesaViewModel = mpesaViewModel,
                context = context,
                modifier = Modifier.fillMaxWidth(0.9f).align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun WalletOperations(){
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Service", style = CC.titleTextStyle(), modifier = Modifier.align(Alignment.Start))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item{
                WalletOperationButton(
                    icon = Icons.Default.Add,
                    label = "Top Up",
                    onClick = {}
                )
            }
            item{
                WalletOperationButton(
                    icon = Icons.Default.Download,
                    label = "Transfer",
                    onClick = {}
                )
            }

            item{
                WalletOperationButton(
                    icon = Icons.Default.Coffee,
                    label = "Tip",
                    onClick = {}
                )
            }
            item{
                WalletOperationButton(
                    icon = Icons.Default.Send,
                    label = "Send",
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun BalanceCard(
    balance: Double,
    showBalance: Boolean,
    showBalanceChange: (Boolean) -> Unit,
    textSize: TextUnit
){
    val brush = Brush.horizontalGradient(
        colors = listOf(CC.primaryColor(), CC.extraSecondaryColor())
    )
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 8.dp), // Add vertical spacing around the card
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // Slightly increased elevation for better shadow effect
    ) {
        Column(
            modifier = Modifier
                .background(brush)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Row: "My Balance" and visibility toggle button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "My Balance",
                    style = CC.titleTextStyle().copy(fontSize = textSize * 0.7f)
                )
                IconButton(
                    onClick = { showBalanceChange(!showBalance) },
                    modifier = Modifier.size(32.dp) // Adjust button size for better proportion
                ) {
                    Icon(
                        imageVector = if (showBalance) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle balance visibility",
                        tint = CC.secondaryColor() // Apply theme-based tint
                    )
                }
            }

            // Balance display with conditional blur
            Spacer(modifier = Modifier.height(12.dp)) // Add spacing between rows
            Text(
                text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(balance),
                style = CC.titleTextStyle()
                    .copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = textSize * 1.2f,
                        color = CC.textColor() // Ensure text is visible on any background
                    ),
                modifier = Modifier.blur(if (showBalance) 0.dp else 8.dp)
            )

            // Divider for better visual separation
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Row: "Total Spent this month" details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total spent this month",
                    style = CC.contentTextStyle().copy(fontSize = textSize * 0.6f)
                )
                Text(
                    text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(1300.6),
                    style = CC.contentTextStyle().copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

/**
 * Custom button for wallet operations.
 * @param icon Icon for the button.
 * @param label Label text for the button.
 * @param onClick Action to perform when the button is clicked.
 */
@Composable
fun WalletOperationButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val cardSize = CC.screenWidth() * 0.24f

    Card(
        modifier = Modifier.size(cardSize),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CC.primaryColor()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .background(CC.secondaryColor(), shape = CircleShape)
                    .clip(CircleShape)
                    .size(cardSize * 0.5f),
                contentAlignment = Alignment.Center
            ){
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CC.extraSecondaryColor(),
                modifier = Modifier.size(cardSize * 0.2f)
            )}

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = CC.contentTextStyle()
            )
        }
    }
}



@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(0.9f),
        colors = CardDefaults.cardColors(containerColor = CC.primaryColor())
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (transaction.type) {
                        TransactionType.TRANSFER -> Icons.AutoMirrored.Default.Send
                        TransactionType.WITHDRAWAL -> Icons.Default.Download
                        TransactionType.TIP -> Icons.Default.Coffee
                        TransactionType.DEPOSIT -> Icons.Default.Add
                    },
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = CC.secondaryColor()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = transaction.recipient,
                        style = CC.titleTextStyle().copy(fontSize = CC.textSize() * 0.7f)
                    )
                    Text(
                        text = transaction.date,
                        style =CC.contentTextStyle().copy(fontSize = CC.textSize() * 0.6f)
                    )
                }
            }
            Text(
                text = NumberFormat.getCurrencyInstance(Locale.US).format(transaction.amount),
                color = if (transaction.amount >= 0)
                    Color(0xff347928)
                else
                    MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                fontSize = CC.textSize() * 0.7f
            )
        }
        HorizontalDivider(color = CC.secondaryColor().copy(alpha = 0.5f), thickness = 1.dp)
    }
}