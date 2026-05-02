package com.wiihope.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText
import java.time.LocalDate
import java.time.LocalDateTime

fun saludar(): String {
    val hour = LocalDateTime.now().hour
    return when {
        hour < 12 -> "Buenos dias"
        hour < 18 -> "Buenas tardes"
        else -> "Buenas noches"
    }
}

fun wiDia(): String {
    val now = LocalDate.now()
    val dias = listOf("Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado")
    val meses = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
    return "${dias[now.dayOfWeek.value % 7]}, ${now.dayOfMonth} ${meses[now.monthValue - 1]}"
}

suspend fun SnackbarHostState.ok(message: String) = showSnackbar(message)
suspend fun SnackbarHostState.err(message: String) = showSnackbar(message)

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    intensity: Float = 0.55f,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val shape = WiCss.glassShape(intensity)
    if (onClick == null) {
        Card(
            modifier = modifier,
            shape = shape,
            colors = WiCss.glassColors(intensity),
            border = WiCss.glassBorder(intensity),
            content = { Column(Modifier.padding(18.dp)) { content() } },
        )
    } else {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = WiCss.glassColors(intensity),
            border = WiCss.glassBorder(intensity),
            content = { Column(Modifier.padding(18.dp)) { content() } },
        )
    }
}

@Composable
fun WiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    loading: Boolean = false,
    color: Color = WiCss.primary,
) {
    val contentColor = if (color == WiCss.gold) WiCss.black else WiCss.white
    Button(
        onClick = onClick,
        enabled = !loading,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = contentColor),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    ) {
        if (loading) {
            CircularProgressIndicator(color = contentColor, strokeWidth = 2.dp)
        } else if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        }
        if (!loading) {
            Text(text, modifier = Modifier.padding(start = if (icon == null) 0.dp else 8.dp), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WiField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null, tint = WiCss.primary, modifier = Modifier.size(20.dp)) } },
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        minLines = minLines,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = WiCss.secondary,
            unfocusedBorderColor = WiCss.goldSoft.copy(alpha = 0.70f),
            focusedContainerColor = WiCss.white.copy(alpha = 0.45f),
            unfocusedContainerColor = WiCss.white.copy(alpha = 0.26f),
            focusedLabelColor = WiCss.primary,
            cursorColor = WiCss.primary,
        ),
    )
}

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Inbox,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(icon, contentDescription = null, tint = WiCss.primary.copy(alpha = 0.45f), modifier = Modifier.size(28.dp))
        Text(message, style = WiText.h3, modifier = Modifier.padding(top = 12.dp))
        if (actionText != null && onAction != null) {
            WiButton(actionText, onAction, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun StatusPill(text: String, color: Color, icon: ImageVector? = null) {
    Card(
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.20f)),
        shape = RoundedCornerShape(999.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            if (icon != null) Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Text(text, style = WiText.tiny.copy(color = color, fontWeight = FontWeight.Bold))
        }
    }
}

@Stable
data class UiMessage(val text: String, val isError: Boolean = false)
