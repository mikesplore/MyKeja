package com.mike.hms.houses.statement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mike.hms.HMSPreferences
import com.mike.hms.model.statements.StatementEntity
import com.mike.hms.viewmodel.StatementViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import com.mike.hms.ui.theme.CommonComponents as CC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatementsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    statementViewModel: StatementViewModel = hiltViewModel(),

    ) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedHouse by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val statements by statementViewModel.statements.collectAsState()

    LaunchedEffect(Unit) {
        statementViewModel.getStatements(HMSPreferences.userId.value)
    }


    // Filter statements
    val filteredStatements = statements.filter { statement ->
        (selectedDate.isEmpty() || statement.date == selectedDate) &&
                (selectedHouse.isEmpty() || statement.houseID == selectedHouse)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Statements",
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
                    IconButton(onClick = { statementViewModel.getStatements(HMSPreferences.userId.value) }) {
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

                        // House Filter
                        OutlinedCard(
                            modifier = Modifier.weight(1f),
                            onClick = { /* Show house picker */ }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    selectedHouse.ifEmpty { "Select House" },
                                    style = CC.contentTextStyle()
                                )
                                Icon(
                                    Icons.Default.Home,
                                    contentDescription = "Select House",
                                    tint = CC.secondaryColor()
                                )
                            }
                        }
                    }
                }
            }

            // Statements Table
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
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TableHeader("ID", screenWidth)
                        TableHeader("Amount", screenWidth)
                        TableHeader("Date", screenWidth)
                        TableHeader("House", screenWidth)
                    }

                    // Table Content
                    if (filteredStatements.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(filteredStatements) { statement ->
                                TableRow(statement, screenWidth)
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
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(),
                showModeToggle = false
            )
        }
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
private fun TableRow(statement: StatementEntity, screenWidth: Dp) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = CC.primaryColor()
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableCell(statement.statementID, screenWidth)
            TableCell(formatAmount(statement.amount), screenWidth, CC.secondaryColor())
            TableCell(formatDate(statement.date), screenWidth)
            TableCell(statement.houseID, screenWidth)
        }
    }
}

@Composable
private fun TableCell(text: String, screenWidth: Dp, textColor: Color = CC.textColor()) {
    Text(
        text = text,
        style = CC.contentTextStyle().copy(color = textColor),
        modifier = Modifier
            .height(screenWidth * 0.06f)
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
                "No statements found",
                style = CC.contentTextStyle(),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatAmount(amount: String): String {
    return try {
        "KES ${NumberFormat.getNumberInstance().format(amount.toDouble())}"
    } catch (e: NumberFormatException) {
        amount
    }
}

private fun formatDate(date: String): String {
    return try {
        val parser = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatter =
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) // Keep the desired format
        val parsedDate = parser.parse(date)
        formatter.format(parsedDate)
    } catch (e: Exception) {
        date
    }
}

