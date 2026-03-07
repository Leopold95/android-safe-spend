package com.alexandr.safespend.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = SecondaryYellow,
    tertiary = PrimaryGreen,
    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceVariant,
    onBackground = OnBackground,
    onSurface = OnSurface,
    error = Error,
    onError = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    outline = Border
)

@Composable
fun AndroidsafespendTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorTokens = ColorTokens(
        primaryAccent = PrimaryGreen,
        secondaryAccent = SecondaryYellow,
        background = Background,
        surface = Surface,
        surfaceVariant = SurfaceVariant,
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        border = Border,
        error = Error,
        success = Success,
        warning = Warning
    )
    val shapeTokens = ShapeTokens()
    val dimensionTokens = DimensionTokens()
    val typographyTokens = Typography.toTokens()

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = shapeTokens.shapes,
    ) {
        CompositionLocalProvider(
            LocalColorTokens provides colorTokens,
            LocalTypographyTokens provides typographyTokens,
            LocalShapeTokens provides shapeTokens,
            LocalDimensionTokens provides dimensionTokens,
            content = content
        )
    }
}
