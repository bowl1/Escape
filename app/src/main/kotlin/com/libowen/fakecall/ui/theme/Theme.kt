package com.libowen.fakecall.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FakeCallColorScheme = darkColorScheme(
    primary          = PurplePrimary,
    onPrimary        = TextPrimary,
    primaryContainer = PurpleDeep,
    secondary        = CyanAccent,
    onSecondary      = DarkBackground,
    tertiary         = GreenAccent,
    background       = DarkBackground,
    surface          = DarkSurface,
    surfaceVariant   = DarkCard,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error            = RedError,
    outline          = TextDisabled
)

@Composable
fun FakeCallTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FakeCallColorScheme,
        typography  = Typography,
        content     = content
    )
}
