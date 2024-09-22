package com.mike.hms.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.mike.hms.R

private val DarkColorScheme = darkColorScheme(
    primary = Black,
    secondary = BlackSchemeTextColor,
    tertiary = Blue,
    background = Black,
    onPrimary = Green,
    onSecondary = Purple,
    onBackground = whiteSurfaceContainer,
)

private val LightColorScheme = lightColorScheme(
    primary = White,
    secondary = WhiteSchemeTextColor,
    tertiary = Red,
    onPrimary = Purple,
    onSecondary = Green,
    onBackground = blackSurfaceContainer,
)

val LibreFranklin = FontFamily(
    Font(R.font.librefranklinregular, FontWeight.Normal),

    )

@Composable
fun HostelManagementSystemTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}