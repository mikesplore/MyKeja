package com.mike.hms.houses.statement

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mike.hms.HMSPreferences
import com.mike.hms.houses.formatNumber
import com.mike.hms.model.transactions.TransactionEntity
import com.mike.hms.model.transactions.TransactionType
import com.mike.hms.model.transactions.TransactionViewModel
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    context: Context

) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedHouse by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val transactions by transactionViewModel.transactions.collectAsState()

    LaunchedEffect(Unit) {
        transactionViewModel.fetchTransactions(HMSPreferences.userId.value)
    }


    // Filter transactions
    val filteredTransactions = transactions.filter { transaction ->
        (selectedDate.isEmpty() || transaction.date == selectedDate) &&
                (selectedHouse.isEmpty() || transaction.transactionType.name == selectedHouse)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Transactions",
                        style = CC.titleTextStyle(),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = "Back",
                            tint = CC.textColor()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CC.primaryColor()
                ),
                actions = {
                    // Add a refresh action
                    IconButton(onClick = { transactionViewModel.fetchTransactions(HMSPreferences.userId.value) }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = CC.textColor()
                        )
                    }
                }
            )
        },
        containerColor = CC.primaryColor()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filters Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CC.extraSecondaryColor()
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Filters",
                        style = CC.contentTextStyle().copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Date Filter
                        OutlinedCard(
                            modifier = Modifier.weight(1f),
                            onClick = { showDatePicker = true }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    selectedDate.ifEmpty { "Select Date" },
                                    style = CC.contentTextStyle()
                                )
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "Select Date",
                                    tint = CC.secondaryColor()
                                )
                            }
                        }

                    }
                }
            }

            // Transactions Table
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = CC.extraSecondaryColor()
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CC.secondaryColor().copy(alpha = 0.1f))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableHeader("No", screenWidth)
                        TableHeader("ID", screenWidth)
                        TableHeader("Amount (Ksh)", screenWidth)
                        TableHeader("Date", screenWidth)
                    }

                    // Table Content
                    if (filteredTransactions.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            itemsIndexed(filteredTransactions) { index, transaction ->
                                TableRow(
                                    index = index + 1,
                                    transaction = transaction,
                                    screenWidth = screenWidth,
                                    context = context
                                ) // Start counting from 1
                            }
                            item {
                                SummaryTable(filteredTransactions)
                            }
                        }

                    } else {
                        EmptyState()
                    }
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        CC.DatePicker(
            onDateSelected = { selectedDate = it },
            onShowDatePickerChange = { showDatePicker = it }
        )
    }
}


@Composable
private fun TableHeader(text: String, screenWidth: Dp) {

    Text(
        text = text,
        style = CC.contentTextStyle().copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .width(screenWidth / 4.5f)
            .padding(horizontal = 4.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TableRow(
    index: Int,
    transaction: TransactionEntity,
    screenWidth: Dp,
    modifier: Modifier = Modifier,
    context: Context
) {
    val clipboardManager = LocalClipboardManager.current
    var isExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = CC.primaryColor()
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column {
            // Main Row (Always visible)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(index.toString(), screenWidth)
                TableCell(transaction.transactionID, screenWidth)
                TableCell(
                    formatNumber(transaction.amount.toInt()),
                    screenWidth,
                    if (transaction.transactionType == TransactionType.ADDITION) Color.Green else Color.Red
                )
                TableCell(CC.formatDateToShortDate(transaction.date), screenWidth)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(rotationState)
                )
            }

            // Expanded Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Transaction Details
                    TransactionDetailItem("Transaction ID", transaction.transactionID)
                    TransactionDetailItem("Amount (Ksh)", formatNumber(transaction.amount.toInt()))
                    TransactionDetailItem("Date", CC.formatDateToShortDate(transaction.date))
                    TransactionDetailItem("Payment Method", transaction.paymentMethod.toString())
                    TransactionDetailItem(
                        "Transaction Type",
                        transaction.transactionType.toString()
                    )
                    TransactionDetailItem("User ID", transaction.userId)

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = CC.secondaryColor().copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Actions Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionButton(
                            icon = Icons.Default.ContentCopy,
                            text = "Copy",
                            onClick = {
                                // Create formatted transaction details string
                                val details = """
                                    Transaction ID: ${transaction.transactionID}
                                    Amount (Ksh): ${formatNumber(transaction.amount.toInt())}
                                    Date: ${CC.formatDateToShortDate(transaction.date)}
                                    Payment Method: ${transaction.paymentMethod}
                                    Transaction Type: ${transaction.transactionType}
                                    User ID: ${transaction.userId}
                                """.trimIndent()

                                // Copy to clipboard

                                clipboardManager.setText(AnnotatedString(details))
                            }
                        )

                        ActionButton(
                            icon = Icons.Default.Share,
                            text = "Share",
                            onClick = {
                                // Create share intent
                                val details = """
                                    Transaction Details
                                    ----------------
                                    Transaction ID: ${transaction.transactionID}
                                    Amount: ${formatNumber(transaction.amount.toInt())}
                                    Date: ${CC.formatDateToShortDate(transaction.date)}
                                    Payment Method: ${transaction.paymentMethod}
                                    Transaction Type: ${transaction.transactionType}
                                    User ID: ${transaction.userId}
                                """.trimIndent()

                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, details)
                                    type = "text/plain"
                                }

                                context.startActivity(
                                    Intent.createChooser(
                                        sendIntent,
                                        "Share Transaction"
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = CC.contentTextStyle().copy(
                color = CC.textColor().copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        )
        Text(
            text = value,
            style = CC.contentTextStyle().copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = CC.secondaryColor()
        ),
        border = BorderStroke(1.dp, CC.secondaryColor().copy(alpha = 0.2f))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = text,
                style = CC.contentTextStyle().copy(fontSize = 14.sp)
            )
        }
    }
}

@Composable
private fun TableCell(
    text: String,
    screenWidth: Dp,
    textColor: Color = CC.textColor()
) {
    val density = LocalDensity.current
    val textSize = with(density) { (screenWidth * 0.03f).toSp() }

    Text(
        text = text,
        style = CC.contentTextStyle().copy(color = textColor, fontSize = textSize),
        modifier = Modifier
            .width(screenWidth / 4.5f)
            .padding(horizontal = 4.dp),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}


@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = CC.secondaryColor()
            )
            Text(
                "No transactions found",
                style = CC.contentTextStyle(),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun SummaryTable(transactions: List<TransactionEntity>) {
    val totalAmountIn = transactions
        .filter { it.transactionType == TransactionType.ADDITION }
        .sumOf { it.amount.toInt() }

    val totalAmountOut = transactions
        .filter { it.transactionType == TransactionType.SUBTRACTION }
        .sumOf { it.amount.toInt() }

    val balance = totalAmountIn - totalAmountOut

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = CC.extraSecondaryColor()
        ),
        shape = RoundedCornerShape(12.dp) // Rounded corners for better aesthetics
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Space between sections
        ) {
            Text(
                text = "Transaction Summary",
                style = CC.titleTextStyle(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.5f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Credit",
                        style = CC.titleTextStyle()
                    )
                    Text(
                        text = formatNumber(totalAmountIn),
                        style = CC.contentTextStyle(),
                        color = Color.Green
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Debit",
                        style = CC.titleTextStyle()
                    )
                    Text(
                        text = formatNumber(totalAmountOut),
                        style = CC.contentTextStyle(),
                        color = Color.Red
                    )
                }
            }

            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.5f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Balance Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Balance",
                    style = CC.titleTextStyle()
                )
                Text(
                    text = formatNumber(balance),
                    style = CC.contentTextStyle(),
                    color = if (balance >= 0) Color.Green else Color.Red
                )
            }
        }
    }
}





