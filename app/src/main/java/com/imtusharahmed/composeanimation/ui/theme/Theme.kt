package com.imtusharahmed.composeanimation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DeepObsidianColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = ObsidianBase,
    primaryContainer = ObsidianElevated,
    onPrimaryContainer = ElectricCyan,
    secondary = CyberViolet,
    onSecondary = TextPrimary,
    secondaryContainer = ObsidianCard,
    onSecondaryContainer = ElectricBlue,
    tertiary = RadiantSunset,
    onTertiary = TextPrimary,
    tertiaryContainer = ObsidianCard,
    onTertiaryContainer = AmberGlow,
    background = ObsidianBase,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = ObsidianElevated,
    onSurfaceVariant = TextSecondary,
    outline = ObsidianBorder,
    outlineVariant = ObsidianBorderSubtle
)

@Composable
fun ComposeAnimationTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DeepObsidianColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
