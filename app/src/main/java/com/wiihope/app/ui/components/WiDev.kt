package com.wiihope.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiiFontFamily
import com.wiihope.app.ui.theme.WiText
import com.wiihope.app.ui.theme.softGlassShadow
import java.time.LocalDate
import java.time.LocalDateTime

// ── Helpers de tiempo ──────────────────────────────────────────────────────
fun saludar(): String = when (LocalDateTime.now().hour) {
    in 0..11  -> "Buenos días"
    in 12..17 -> "Buenas tardes"
    else      -> "Buenas noches"
}

fun wiDia(): String {
    val now = LocalDate.now()
    val dias = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
    val meses = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
    return "${dias[now.dayOfWeek.value % 7]}, ${now.dayOfMonth} ${meses[now.monthValue - 1]}"
}

suspend fun SnackbarHostState.ok(message: String) = showSnackbar(message)
suspend fun SnackbarHostState.err(message: String) = showSnackbar(message)

// ── GlassCard — Luminous Sanctuary signature component ────────────────────
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    intensity: Float = 0.55f,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val shape = WiCss.glassShape(intensity)
    val cardColors = WiCss.glassColors(intensity)
    val border = WiCss.glassBorder(intensity)

    if (onClick == null) {
        Card(
            modifier = modifier.softGlassShadow(),
            shape = shape,
            colors = cardColors,
            border = border,
            content = { Column(Modifier.padding(18.dp)) { content() } },
        )
    } else {
        Card(
            onClick = onClick,
            modifier = modifier.softGlassShadow(),
            shape = shape,
            colors = cardColors,
            border = border,
            content = { Column(Modifier.padding(18.dp)) { content() } },
        )
    }
}

// ── GoldPill — tag/chip dorado luminoso ───────────────────────────────────
@Composable
fun GoldPill(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(WiCss.gold.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = text.uppercase(),
            style = WiText.label,
            color = WiCss.secondary,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

// ── WiButton — primary & outlined variants ────────────────────────────────
@Composable
fun WiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    loading: Boolean = false,
    color: Color = WiCss.primary,
    outlined: Boolean = false,
) {
    if (outlined) {
        Button(
            onClick = onClick,
            enabled = !loading,
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = WiCss.secondary,
                disabledContainerColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
            border = BorderStroke(1.5.dp, WiCss.goldSoft),
        ) {
            _ButtonContent(text, icon, loading, WiCss.secondary)
        }
    } else {
        val contentColor = when {
            color == WiCss.gold || color == WiCss.goldSoft -> WiCss.black
            color == WiCss.primary -> WiCss.white
            else -> WiCss.white
        }
        Button(
            onClick = onClick,
            enabled = !loading,
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = contentColor),
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        ) {
            _ButtonContent(text, icon, loading, contentColor)
        }
    }
}

// Gold CTA button (solid gold with dark text — Luminous Sanctuary primary action)
@Composable
fun WiGoldButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = !loading,
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(WiCss.gradGoldSoft),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = WiCss.black,
        ),
        shape = RoundedCornerShape(999.dp),
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 16.dp),
    ) {
        _ButtonContent(text, icon, loading, WiCss.black)
    }
}

@Composable
private fun _ButtonContent(text: String, icon: ImageVector?, loading: Boolean, contentColor: Color) {
    if (loading) {
        CircularProgressIndicator(color = contentColor, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
    } else {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text,
            fontFamily = WiiFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
    }
}

// ── WiField — Luminous Sanctuary polished input ───────────────────────────
@Composable
fun WiField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = WiiFontFamily) },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = leadingIcon?.let {
            { Icon(it, contentDescription = null, tint = WiCss.secondary, modifier = Modifier.size(20.dp)) }
        },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        minLines = minLines,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = WiCss.secondary,
            unfocusedBorderColor = WiCss.goldSoft.copy(alpha = 0.60f),
            focusedContainerColor = WiCss.white.copy(alpha = 0.55f),
            unfocusedContainerColor = WiCss.white.copy(alpha = 0.28f),
            focusedLabelColor = WiCss.secondary,
            unfocusedLabelColor = WiCss.gray,
            cursorColor = WiCss.secondary,
            focusedTextColor = WiCss.text700,
            unfocusedTextColor = WiCss.text700,
        ),
    )
}

// ── EmptyState ────────────────────────────────────────────────────────────
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Inbox,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(WiCss.gold.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = WiCss.secondary, modifier = Modifier.size(30.dp))
        }
        Text(message, style = WiText.h3, modifier = Modifier.padding(top = 16.dp), textAlign = TextAlign.Center)
        if (actionText != null && onAction != null) {
            WiButton(actionText, onAction, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

// ── StatusPill ────────────────────────────────────────────────────────────
@Composable
fun StatusPill(text: String, color: Color, icon: ImageVector? = null) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.10f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.22f)),
        shape = RoundedCornerShape(999.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            if (icon != null) Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
            Text(text, style = WiText.tiny.copy(color = color, fontWeight = FontWeight.Bold))
        }
    }
}

@Stable
data class UiMessage(val text: String, val isError: Boolean = false)
