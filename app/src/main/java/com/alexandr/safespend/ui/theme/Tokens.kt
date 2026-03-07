package com.alexandr.safespend.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ==================== Color Tokens ====================
data class ColorTokens(
    val primaryAccent: Color = PrimaryGreen,
    val secondaryAccent: Color = SecondaryYellow,
    val background: Color = Background,
    val surface: Color = Surface,
    val surfaceVariant: Color = SurfaceVariant,
    val textPrimary: Color = TextPrimary,
    val textSecondary: Color = TextSecondary,
    val border: Color = Border,
    val error: Color = Error,
    val success: Color = Success,
    val warning: Color = Warning,
    val disabled: Color = DisabledGray,
    val lightGray: Color = LightGray,
    val darkGray: Color = DarkGray
)

// ==================== Typography Tokens ====================
data class TypographyTokens(
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle
)

fun Typography.toTokens(): TypographyTokens = TypographyTokens(
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall
)

// ==================== Shape Tokens ====================
data class ShapeTokens(
    val smallRadius: Dp = 8.dp,
    val mediumRadius: Dp = 12.dp,
    val largeRadius: Dp = 20.dp,
    val shapes: Shapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(20.dp)
    )
)

// ==================== Dimension Tokens ====================
data class DimensionTokens(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val cardElevation: Dp = 2.dp,
    val modalElevation: Dp = 8.dp,
    val fabElevation: Dp = 6.dp,
    val borderWidth: Dp = 1.dp
)

// ==================== Composition Locals ====================
val LocalColorTokens = compositionLocalOf { ColorTokens() }
val LocalTypographyTokens = compositionLocalOf { Typography.toTokens() }
val LocalShapeTokens = compositionLocalOf { ShapeTokens() }
val LocalDimensionTokens = compositionLocalOf { DimensionTokens() }

// ==================== Theme Facade ====================
object LocalAppTheme {
    /**
     * Поточні кольорові токени.
     */
    val colors: ColorTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalColorTokens.current

    /**
     * Поточні типографічні токени.
     */
    val typography: TypographyTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalTypographyTokens.current

    /**
     * Поточні токени форм (скруглення, шейпи карток тощо).
     */
    val shapes: ShapeTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalShapeTokens.current

    /**
     * Поточні розмірні токени (розміри елементів, відступи, висоти).
     */
    val dimen: DimensionTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensionTokens.current
}
