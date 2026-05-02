package com.wiihope.app.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val WiiFontFamily = FontFamily.SansSerif

object WiCss {
    val primary = Color(0xFF705D00)
    val secondary = Color(0xFFC9A800)
    val gold = Color(0xFFFFDA34)
    val goldSoft = Color(0xFFFFE066)
    val goldGlow = Color(0x4DFFDA34)
    val bgBase = Color(0xFFFFF8D1)
    val bgSoft = Color(0xFFFFFDE8)
    val cream = Color(0xFFFFF9E5)
    val surfaceHigh = Color(0xFFF0E9C3)
    val surfaceVariant = Color(0xFFEAE3BD)
    val glassWhite = Color(0x6BFFFFFF)
    val white = Color.White
    val black = Color(0xFF1F1C05)
    val gray = Color(0xFF7E7760)
    val grayLight = Color(0xFFCFC6AC)
    val text700 = Color(0xFF1F1C05)
    val text500 = Color(0xFF4C4733)
    val text300 = Color(0xFF7E7760)
    val error = Color(0xFFBA1A1A)
    val errorContainer = Color(0xFFFFDAD6)
    val warning = Color(0xFFFF9800)
    val info = Color(0xFF2196F3)

    val gradBase = Brush.radialGradient(
        colors = listOf(Color(0xFFFFFDE8), Color(0xFFFFF8D1)),
        radius = 1200f,
    )
    val gradPremium = Brush.linearGradient(
        colors = listOf(Color(0xFFFFFDE8), Color(0xFFFFF9E5), Color(0xFFFFF8D1)),
    )
    val gradHero = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFF9E5), Color(0xFFFFF3B0), Color(0xFFFFF9E5)),
    )
    val gradGold = Brush.linearGradient(
        colors = listOf(Color(0xFFFFDA34), Color(0xFFC9A800)),
    )
    val gradGoldSoft = Brush.linearGradient(
        colors = listOf(Color(0xFFFFE16C), Color(0xFFFFDA34)),
    )

    val r8 = 8.dp
    val r12 = 12.dp
    val r16 = 16.dp
    val r20 = 20.dp
    val r24 = 24.dp
    val r28 = 28.dp
    val r32 = 32.dp
    val s8 = 8.dp
    val s12 = 12.dp
    val s16 = 16.dp
    val s20 = 20.dp
    val s24 = 24.dp
    val s32 = 32.dp

    val padM = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    val padL = PaddingValues(horizontal = 20.dp, vertical = 16.dp)

    fun glassShape(intensity: Float = 0.55f) =
        RoundedCornerShape(if (intensity > 0.75f) r24 else r20)

    fun glassBorder(intensity: Float = 0.55f) =
        BorderStroke(1.dp, goldSoft.copy(alpha = 0.40f + intensity * 0.25f))

    @Composable
    fun glassColors(intensity: Float = 0.55f) = CardDefaults.cardColors(
        containerColor = white.copy(alpha = 0.20f + intensity * 0.36f),
        contentColor = text700,
    )
}

private val LightColors: ColorScheme = lightColorScheme(
    primary = WiCss.primary,
    onPrimary = WiCss.white,
    primaryContainer = WiCss.gold,
    onPrimaryContainer = Color(0xFF221B00),
    secondary = Color(0xFF725C00),
    onSecondary = WiCss.white,
    secondaryContainer = Color(0xFFFED00B),
    onSecondaryContainer = Color(0xFF6F5900),
    tertiary = Color(0xFF705D00),
    tertiaryContainer = Color(0xFFFFDA43),
    background = WiCss.bgBase,
    surface = WiCss.cream,
    surfaceVariant = WiCss.surfaceVariant,
    onSurface = WiCss.text700,
    onSurfaceVariant = WiCss.text500,
    outline = WiCss.gray,
    outlineVariant = WiCss.grayLight,
    error = WiCss.error,
    errorContainer = WiCss.errorContainer,
)

private val WiiHopeTypography = Typography(
    displayLarge = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 48.sp, lineHeight = 52.sp),
    displayMedium = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 36.sp, lineHeight = 44.sp),
    headlineLarge = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.Medium, fontSize = 28.sp, lineHeight = 36.sp),
    headlineMedium = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.Medium, fontSize = 22.sp, lineHeight = 30.sp),
    headlineSmall = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.Medium, fontSize = 18.sp, lineHeight = 24.sp),
    titleLarge = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
    titleSmall = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 26.sp),
    bodyMedium = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 22.sp),
    bodySmall = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 18.sp),
    labelLarge = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, letterSpacing = 0.05.sp),
    labelMedium = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = WiiFontFamily, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.04.sp),
)

object WiText {
    val display = TextStyle(fontFamily = WiiFontFamily, fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = WiCss.text700, lineHeight = 38.sp)
    val h1 = TextStyle(fontFamily = WiiFontFamily, fontSize = 26.sp, fontWeight = FontWeight.SemiBold, color = WiCss.text700, lineHeight = 32.sp)
    val h2 = TextStyle(fontFamily = WiiFontFamily, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = WiCss.text700, lineHeight = 28.sp)
    val h3 = TextStyle(fontFamily = WiiFontFamily, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = WiCss.text700, lineHeight = 22.sp)
    val body = TextStyle(fontFamily = WiiFontFamily, fontSize = 14.sp, fontWeight = FontWeight.Normal, color = WiCss.text500, lineHeight = 22.sp)
    val small = TextStyle(fontFamily = WiiFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = WiCss.text300, lineHeight = 18.sp)
    val tiny = TextStyle(fontFamily = WiiFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = WiCss.text300, lineHeight = 15.sp)
    val label = TextStyle(fontFamily = WiiFontFamily, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = WiCss.text300, letterSpacing = 0.8.sp)
    val gold = TextStyle(fontFamily = WiiFontFamily, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WiCss.secondary)
}

fun Modifier.premiumBackground(): Modifier = background(WiCss.gradPremium)

fun Modifier.softGlassShadow(): Modifier = shadow(
    elevation = 20.dp,
    shape = RoundedCornerShape(WiCss.r20),
    ambientColor = WiCss.primary.copy(alpha = 0.10f),
    spotColor = WiCss.gold.copy(alpha = 0.18f),
)

fun Modifier.goldGlowShadow(): Modifier = shadow(
    elevation = 12.dp,
    shape = RoundedCornerShape(WiCss.r16),
    ambientColor = WiCss.gold.copy(alpha = 0.08f),
    spotColor = WiCss.gold.copy(alpha = 0.20f),
)

@Composable
fun WiiHopeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = WiiHopeTypography,
        content = content,
    )
}
