package com.mike.hms.houses.bookHouse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mike.hms.ui.theme.CommonComponents
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleCard() {
    var oldDate by remember { mutableStateOf(LocalDate.now()) }
    var newDate by remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    var showOldDatePicker by remember { mutableStateOf(false) }
    var showNewDatePicker by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = CommonComponents.primaryColor()),
        modifier = Modifier

            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = "Reschedule",
                style = CommonComponents.titleTextStyle().copy(fontWeight = FontWeight.Bold),
                color = CommonComponents.textColor()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DateColumn(
                    title = "Old Date",
                    date = oldDate,
                    onDateClick = { showOldDatePicker = true }
                )
                DateColumn(
                    title = "New Date",
                    date = newDate,
                    onDateClick = { showNewDatePicker = true }
                )
            }
        }
    }

    // Date Pickers
    if (showOldDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showOldDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showOldDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOldDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = oldDate.toEpochDay() * 24 * 60 * 60 * 1000),

                )
        }
    }

    if (showNewDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showNewDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showNewDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = newDate.toEpochDay() * 24 * 60 * 60 * 1000),
            )
        }
    }
}

@Composable
fun DateColumn(
    title: String,
    date: LocalDate,
    onDateClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = title,
            style = CommonComponents.bodyTextStyle().copy(fontWeight = FontWeight.Medium),
            color = CommonComponents.textColor()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onDateClick)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select Date",
                tint = CommonComponents.extraPrimaryColor(),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                style = CommonComponents.bodyTextStyle().copy(fontWeight = FontWeight.Bold),
                color = CommonComponents.extraPrimaryColor()
            )
        }
    }
}
