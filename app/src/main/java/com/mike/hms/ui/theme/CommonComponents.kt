package com.mike.hms.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import java.util.UUID

object CommonComponents {

    @Composable
    fun primaryColor(): Color {
        return MaterialTheme.colorScheme.primary
    }

    @Composable
    fun secondaryColor(): Color {
        return MaterialTheme.colorScheme.secondary
    }

    @Composable
    fun extraPrimaryColor(): Color {
        return MaterialTheme.colorScheme.onPrimary
    }

    @Composable
    fun extraSecondaryColor(): Color {
        return MaterialTheme.colorScheme.onSecondary
    }

    @Composable
    fun surfaceContainerColor(): Color {
        return MaterialTheme.colorScheme.onBackground
    }


    @Composable
    fun textColor(): Color {
        return MaterialTheme.colorScheme.secondary
    }

    @Composable
    fun tertiaryColor(): Color {
        return MaterialTheme.colorScheme.tertiary
    }

    @Composable
    fun titleColor(): Color {
        return MaterialTheme.colorScheme.onSurface
    }

    @Composable
    fun titleTextStyle(): TextStyle {
        return TextStyle.Default.copy(
            fontFamily = LibreFranklin,
            color = textColor(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,

            )
    }

    @Composable
    fun radioButtonColors(): RadioButtonColors {
        return RadioButtonDefaults.colors(
            selectedColor = tertiaryColor(),
            unselectedColor = textColor()
        )

    }

    @Composable
    fun buttonColors(): ButtonColors {
        return ButtonDefaults.buttonColors(
            containerColor = tertiaryColor(),
            contentColor = primaryColor()
        )
    }

    @Composable
    fun contentTextStyle(): TextStyle {
        return TextStyle.Default.copy(
            fontFamily = LibreFranklin,
            color = textColor(),
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
        )
    }

    @Composable
    fun bodyTextStyle(): TextStyle {
        return TextStyle.Default.copy(
            fontFamily = LibreFranklin,
            color = textColor(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    }


    //Search TextField
    @Composable
    fun SearchTextField(
        modifier: Modifier = Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: String
    ) {
        OutlinedTextField(
            value = value,
            textStyle = contentTextStyle(),
            onValueChange = { onValueChange(it) }, // Use 'it' to get the updated value
            singleLine = true,
            placeholder = { Text(placeholder, style = contentTextStyle()) },
            shape = RoundedCornerShape(20.dp),
            modifier = modifier,
            colors = outLinedTextFieldColors(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon"
                )
            }
        )
    }

    @Composable
    fun outLinedTextFieldColors(): TextFieldColors {
        return OutlinedTextFieldDefaults.colors(
            focusedBorderColor = secondaryColor(),
            unfocusedBorderColor = textColor(),
            focusedLabelColor = textColor(),
            unfocusedLabelColor = textColor(),
            cursorColor = textColor()
        )
    }

    @Composable
    fun outLinedTextFieldShape(): RoundedCornerShape {
        return RoundedCornerShape(20.dp)
    }


    @Composable
    fun greeting(): String {
        val currentHour = LocalTime.now().hour
        val greeting = when (currentHour) {
            in 0..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }
        return greeting
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topAppBarColors(): TopAppBarColors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = primaryColor(),
            titleContentColor = textColor(),
            navigationIconContentColor = textColor(),
            actionIconContentColor = textColor(),
            scrolledContainerColor = primaryColor()
        )

    @Composable
    fun MyOutlinedTextField(
        modifier: Modifier = Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        placeholder: String,
        singleLine: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text, // Default to text
        imeAction: ImeAction = ImeAction.Done, // Default to Done
        trailingIcon: @Composable (() -> Unit)? = null,
        isError: Boolean = false,
        leadingIcon: @Composable (() -> Unit)? = null,

        ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, style = contentTextStyle()) },
            placeholder = { Text(placeholder, style = contentTextStyle()) },
            modifier = modifier,
            colors = outLinedTextFieldColors(),
            shape = outLinedTextFieldShape(),
            textStyle = contentTextStyle(),
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            leadingIcon = leadingIcon,
            singleLine = singleLine,
            isError = isError,

            )
    }

    @Composable
    fun transformText(text: String): String {
        return if (text.length > 1) {
            text.replaceFirstChar { it.uppercase() }
                .replace("_", " ")
                .substring(1)
                .lowercase(Locale.getDefault())
                .let { text.first().uppercase() + it }
        } else {
            text.replaceFirstChar { it.uppercase() }.replace("_", " ")
        }

    }

    @Composable
    fun getScreenHeight(): Dp {
        return LocalConfiguration.current.screenHeightDp.dp

    }

    @Composable
    fun getScreenWidth(): Dp {
        return LocalConfiguration.current.screenWidthDp.dp

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ConfirmDialog(
        title: String,
        message: String,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit,
        confirmText: String = "Confirm",
        dismissText: String = "Cancel",
        messageColor: Color = textColor(),
        containerColor: Color = extraSecondaryColor(),
        textFieldValue: String? = null,
        onTextFieldValueChange: ((String) -> Unit)? = null, // Only required if TextField is displayed
        keyboardType: KeyboardType = KeyboardType.Unspecified,
        textFieldNumber: Int = 1
    ) {
        AlertDialog(
            containerColor = containerColor,
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    style = titleTextStyle().copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = message,
                        style = contentTextStyle().copy(
                            color = messageColor,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Conditionally show TextField if `textFieldValue` is not null
                    if (textFieldValue != null && onTextFieldValueChange != null) {
                        TextField(
                            value = textFieldValue,
                            onValueChange = onTextFieldValueChange,
                            label = { Text("Input") },
                            textStyle = contentTextStyle(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = keyboardType,
                                imeAction = ImeAction.Done
                            ),
                            colors = outLinedTextFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = buttonColors()
                ) {
                    Text(
                        text = confirmText,
                        style = contentTextStyle()
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = titleColor()
                    )
                ) {
                    Text(
                        text = dismissText,
                        style = contentTextStyle()
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(16.dp)
                .width(getScreenWidth() * 0.8f),
            tonalElevation = 8.dp
        )
    }

    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    fun formatDateToShortDate(date: String): String {
        return try {
            val parser = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formatter =
                SimpleDateFormat("dd/MM/yy", Locale.getDefault()) // Corrected format string
            val parsedDate = parser.parse(date)
            formatter.format(parsedDate)
        } catch (e: Exception) {
            date
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DatePicker(onDateSelected: (String) -> Unit, onShowDatePickerChange: (Boolean) -> Unit) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { onShowDatePickerChange(false) },
            confirmButton = {
                TextButton(onClick = {
                    onShowDatePickerChange(false)
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(Date(millis))
                        onDateSelected(selectedDate) // Pass selected date to the callback
                    }
                }) {
                    Text("OK", style = MaterialTheme.typography.bodyMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { onShowDatePickerChange(false) }) {
                    Text("Cancel", style = MaterialTheme.typography.bodyMedium)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = true
            )
        }

    }


    data class MyCode(
        val id: String = UUID.randomUUID().toString(),
        var code: Int = 0
    )

    // Code Generator
    private fun updateAndGetCode(path: String, onCodeUpdated: (Int) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("Codes").child(path)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val myCode = snapshot.getValue(MyCode::class.java)!!
                    myCode.code += 1
                    database.setValue(myCode).addOnSuccessListener {
                        onCodeUpdated(myCode.code) // Pass the incremented code to the callback
                    }
                } else {
                    val newCode = MyCode(code = 1)
                    database.setValue(newCode).addOnSuccessListener {
                        onCodeUpdated(newCode.code) // Pass the initial code to the callback
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error appropriately (e.g., log it or notify the user)
            }
        })
    }

    fun generateHouseId(onCodeUpdated: (String) -> Unit) {
        updateAndGetCode("House") { code ->
            val houseId = "H$code"
            onCodeUpdated(houseId)
        }
    }

    fun generateUserId(onCodeUpdated: (String) -> Unit) {
        updateAndGetCode("Users") { code ->
            val userId = "User$code"
            onCodeUpdated(userId)
        }
    }

    fun generateUserId(onCodeUpdated: (String) -> Unit, path: String) {
        updateAndGetCode(path) { code ->
            val userId = "User$code"
            onCodeUpdated(userId)
        }
    }

    fun generateCardId(onCodeUpdated: (String) -> Unit) {
        updateAndGetCode("Cards") { code ->
            val cardId = "Card$code"
            onCodeUpdated(cardId)
        }
    }

    fun generateFavouriteId(onCodeUpdated: (String) -> Unit) {
        updateAndGetCode("Favorites") { code ->
            val favouriteId = "Fav$code"
            onCodeUpdated(favouriteId)
        }
    }

    fun generateMpesaId(onCodeUpdated: (String) -> Unit) {
        updateAndGetCode("Mpesa") { code ->
            val mpesaId = "Mpesa$code"
            onCodeUpdated(mpesaId)
        }
    }

    fun generatePayPalId(onCodeUpdated: (String) -> Unit) {
        updateAndGetCode("PayPal") { code ->
            val payPalId = "PayPal$code"
            onCodeUpdated(payPalId)
        }
    }

    fun generateTransactionId(onCodeUpdated: (String) -> Unit) {
        updateAndGetCode("Transaction") { code ->
            val transactionId = "Trans$code"
            onCodeUpdated(transactionId)
        }
    }

}