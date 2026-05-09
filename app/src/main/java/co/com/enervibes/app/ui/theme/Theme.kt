package co.com.enervibes.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Indigo500,
    onPrimary = Slate50,
    primaryContainer = Indigo700,
    onPrimaryContainer = Slate100,
    secondary = Pink500,
    onSecondary = Slate50,
    secondaryContainer = Pink600,
    onSecondaryContainer = Slate100,
    tertiary = Green500,
    onTertiary = Slate50,
    background = Slate900,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate50,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate300,
    outline = Slate600,
    outlineVariant = Slate700,
    error = Red500,
    onError = Slate50,
    errorContainer = Red600,
    onErrorContainer = Slate100,
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo500,
    onPrimary = Slate50,
    primaryContainer = Indigo500,
    onPrimaryContainer = Slate50,
    secondary = Pink500,
    onSecondary = Slate50,
    secondaryContainer = Pink500,
    onSecondaryContainer = Slate50,
    tertiary = Green500,
    onTertiary = Slate50,
    background = Slate900,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate50,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate300,
    outline = Slate600,
    outlineVariant = Slate700,
    error = Red500,
    onError = Slate50,
    errorContainer = Red600,
    onErrorContainer = Slate100,
)

@Composable
fun EnervibesAppTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
