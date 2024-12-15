package com.example.doctorlocationapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Color Definitions
val Green = Color(0xFF4CAF50)
val DarkGray = Color(0xFF212121)
val LightGray = Color(0xFFBDBDBD)
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

private val DarkColorScheme = darkColorScheme(
    primary = Green,
    secondary = DarkGray,
    tertiary = Black
)

private val LightColorScheme = lightColorScheme(
    primary = Green,
    secondary = LightGray,
    tertiary = White
)

@Composable
fun DoctorLocationAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
