package com.mike.hms.ui.theme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
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
    fun extraPrimaryColor(): Color{
        return MaterialTheme.colorScheme.onPrimary
    }

    @Composable
    fun extraSecondaryColor(): Color{
        return MaterialTheme.colorScheme.onSecondary
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
            colors = OutlinedTextFieldDefaults.colors(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon"
                )
            }
        )
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
}