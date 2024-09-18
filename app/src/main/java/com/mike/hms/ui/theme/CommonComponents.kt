package com.mike.hms.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
    fun textColor(): Color {
        return MaterialTheme.colorScheme.secondary
    }

    @Composable
    fun tertiaryColor(): Color {
        return MaterialTheme.colorScheme.tertiary
    }

    @Composable
    fun titleTextStyle(): TextStyle{
        return TextStyle(
            fontFamily = LibreFranklin,
            color = textColor(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }

    @Composable
    fun contentTextStyle(): TextStyle{
        return TextStyle(
            fontFamily = LibreFranklin,
            color = textColor(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    }

    @Composable
    fun bodyTextStyle(): TextStyle{
        return TextStyle(
            fontFamily = LibreFranklin,
            color = textColor(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    }


    @Composable
    fun buttonTextStyle(): TextStyle{
        return TextStyle(
            fontFamily = LibreFranklin,
            color = textColor(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    }


    @Composable
    fun MyOutlinedTextField(
        value: String,
        placeholder: String,
        onValueChange: (String) -> Unit,
        singleLine: Boolean
    ){
        OutlinedTextField(
            value = value,
            textStyle = contentTextStyle(),
            onValueChange = {onValueChange(value)},
            singleLine = singleLine,
            placeholder = { Text(placeholder, style = contentTextStyle()) },
            colors = OutlinedTextFieldDefaults.colors()
        )
    }
}