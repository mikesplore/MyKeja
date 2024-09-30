package com.mike.hms.ui.theme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime

object CommonComponents{

    @Composable
    fun primaryColor(): Color {
        return MaterialTheme.colorScheme.primary
    }

    @Composable
    fun secondaryColor(): Color{
        return MaterialTheme.colorScheme.secondary
    }

    @Composable
    fun extraPrimaryColor(): Color{
        return MaterialTheme.colorScheme.onPrimary
    }

    @Composable
    fun extraSecondaryColor(): Color{
        return MaterialTheme.colorScheme.onSecondary
    }

    @Composable
    fun surfaceContainerColor(): Color{
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
    fun titleTextStyle(): TextStyle{
        return TextStyle.Default.copy(
            fontFamily = LibreFranklin,
            color = textColor(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }

    @Composable
    fun contentTextStyle(): TextStyle{
        return TextStyle.Default.copy(
            fontFamily = LibreFranklin,
            color = textColor(),
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
        )
    }

    @Composable
    fun bodyTextStyle(): TextStyle{
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
    fun outLinedTextFieldColors(): TextFieldColors{
        return OutlinedTextFieldDefaults.colors(
            focusedBorderColor = secondaryColor(),
            unfocusedBorderColor = textColor(),
            focusedLabelColor = textColor(),
            unfocusedLabelColor = textColor(),
            cursorColor = textColor()
        )
    }

    @Composable
    fun outLinedTextFieldShape(): RoundedCornerShape{
        return RoundedCornerShape(20.dp)
    }


    @Composable
    fun AdaptiveSizes(
        modifier: Modifier = Modifier,
        textFraction: Float = 1.0f, // Fraction for text size
        dpFraction: Float = 1.0f,   // Fraction for DP size
        content: @Composable (textSize: TextUnit, dpSize: Dp) -> Unit
    ) {
        BoxWithConstraints(modifier = modifier) {
            val columnWidth = maxWidth
            val density = LocalDensity.current
            val textSize = with(density) { (columnWidth * textFraction).toSp() }
            val dpSize = columnWidth * dpFraction
            content(textSize, dpSize)
        }
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
        trailingIcon: @Composable (() -> Unit)? = null
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
            singleLine = singleLine
        )
    }
}