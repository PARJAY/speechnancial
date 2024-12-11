package com.example.speechnancial.ui.theme

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

// note :

// surfaceContainer

// on primary container = outcome color
// on secondary container = income color
// on teritary container = text transaction type color

// primary = primary text / border stroke color
// secondary = secondary text / border stroke color
// tertiary = actual text app primary color
// onTertiary = actual text app secondary color

private val DarkColorScheme = darkColorScheme(
    background = backgroundDarkTheme,
    surfaceContainer = backgroundContainerDarkTheme,

    onPrimaryContainer = incomeBackgroundDarkTheme,
    onSecondaryContainer = outcomeBackgroundDarkTheme,

    onTertiaryContainer = textOutcomeDark,
    tertiaryContainer = textIncomeDark,

    primary = primaryContentDarkTheme,
    secondary = secondaryContentDarkTheme,

    tertiary = appColorPrimary,
    onTertiary = appColorSecondary,
)

private val LightColorScheme = lightColorScheme(
    background = backgroundLightTheme,
    surfaceContainer = backgroundContainerLightTheme,

    onPrimaryContainer = incomeBackgroundLightTheme,
    onSecondaryContainer = outcomeBackgroundLightTheme,

    onTertiaryContainer = textOutcomeLight,
    tertiaryContainer = textIncomeLight,

    primary = primaryContentLightTheme,
    secondary = secondaryLContentLightTheme,

    tertiary = appColorPrimary,
    onTertiary = appColorSecondary,
)

@Composable
fun SpeechnancialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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

private val lightThemeColorScheme = CustomColorScheme(
    background = Color(0xFFf0f0f0),
    backgroundContainer  = Color(0xFFffffff),

    incomeBackground  = Color(0xFF8BF69A),
    outcomeBackground  = Color(0xFFFA5C5C),

    primaryContent  = Color(0xFF000000),
    secondContent  = Color(0xFF505050),
)

private val darkThemeColorScheme = CustomColorScheme(
    background = Color(0xFF000000),
    backgroundContainer = Color(0xFF101010),

    incomeBackground = Color(0xFF269F37),
    outcomeBackground = Color(0xFFE03838),

    primaryContent = Color(0xFFFFFFFF),
    secondContent = Color(0xFFCCCCCC)
)

data class CustomColorScheme(
    val primary : Color = Color(0xff009BED),
    val secondary : Color = Color(0xffFFBA4B),

    val background : Color,
    val backgroundContainer : Color,

    val incomeBackground : Color,
    val outcomeBackground : Color,

    val primaryContent : Color,
    val secondContent : Color,
)