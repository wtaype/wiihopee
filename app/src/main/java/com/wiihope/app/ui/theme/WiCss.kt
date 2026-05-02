package com.wiihope.app.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
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

object WiCss {
    val primary = Color(0xFF705D00)
    val secondary = Color(0xFFC9A800)
    val gold = Color(0xFFFFDA34)
    val goldSoft = Color(0xFFFFE066)
    val bgLight = Color(0xFFFFF8D1)
    val bgSoft = Color(0xFFFFFDE8)
    val bgDark = Color(0xFF705D00)
    val cream = Color(0xFFFFF9E5)
    val white = Color.White
    val black = Color(0xFF1F1C05)
    val gray = Color(0xFF7E7760)
    val grayLight = Color(0xFFF6EFC8)
    val text700 = Color(0xFF1F1C05)
    val text500 = Color(0xFF4C4733)
    val text300 = Color(0xFF665500)
    val error = Color(0xFFE53935)
    val warning = Color(0xFFFF9800)
    val info = Color(0xFF2196F3)

    val gradGreen = Brush.linearGradient(listOf(gold, secondary))
    val gradPremium = Brush.linearGradient(listOf(Color(0xFFFFF8D1), Color(0xFFFFFDE8), Color(0xFFFFF9E5)))
    val gradHero = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0x66FFDA34), Color(0x33FFFFFF)))

    val r8 = 8.dp
    val r12 = 12.dp
    val r16 = 16.dp
    val r20 = 20.dp
    val r28 = 28.dp
    val s8 = 8.dp
    val s12 = 12.dp
    val s16 = 16.dp
    val s24 = 24.dp
    val s32 = 32.dp

    val padM = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    val padL = PaddingValues(horizontal = 20.dp, vertical = 16.dp)

    fun glassShape(intensity: Float = 0.55f) = RoundedCornerShape(if (intensity > 0.7f) r28 else r20)
    fun glassBorder(intensity: Float = 0.55f) = BorderStroke(1.dp, goldSoft.copy(alpha = 0.48f + intensity * 0.20f))
    @Composable
    fun glassColors(intensity: Float = 0.55f) = CardDefaults.cardColors(
        containerColor = white.copy(alpha = 0.26f + intensity * 0.30f),
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
    background = WiCss.cream,
    surface = WiCss.white,
    surfaceVariant = Color(0xFFEAE3BD),
    onSurface = WiCss.text700,
    error = WiCss.error,
)

object WiText {
    val h1 = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = WiCss.text700, fontFamily = FontFamily.SansSerif)
    val h2 = TextStyle(fontSize = 23.sp, fontWeight = FontWeight.SemiBold, color = WiCss.text700, fontFamily = FontFamily.SansSerif)
    val h3 = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = WiCss.text700, fontFamily = FontFamily.SansSerif)
    val body = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal, color = WiCss.text500, fontFamily = FontFamily.SansSerif)
    val small = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = WiCss.text300, fontFamily = FontFamily.SansSerif)
    val tiny = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium, color = WiCss.text300, fontFamily = FontFamily.SansSerif)
}

fun Modifier.premiumBackground(): Modifier = background(WiCss.gradPremium)

fun Modifier.softGlassShadow(): Modifier = shadow(
    elevation = 18.dp,
    shape = RoundedCornerShape(WiCss.r20),
    ambientColor = WiCss.primary.copy(alpha = 0.14f),
    spotColor = WiCss.gold.copy(alpha = 0.12f),
)

@Composable
fun WiiHopeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = MaterialTheme.typography,
        content = content,
    )
}
