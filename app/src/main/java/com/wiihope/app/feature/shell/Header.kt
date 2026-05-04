package com.wiihope.app.feature.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiihope.app.core.app.Wii
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

@Composable
internal fun Header(
    onHome: () -> Unit,
    onMessages: () -> Unit,
    onNotifications: () -> Unit,
    onMenu: () -> Unit,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .statusBarsPadding()
            .height(54.dp)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            Wii.app,
            style = WiText.h3.copy(color = WiCss.primary, fontSize = 19.sp),
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onHome,
                ),
        )
        if (showBack) {
            HeaderIcon(Icons.AutoMirrored.Rounded.ArrowBack, "Volver", onBack)
        }
        HeaderIcon(Icons.Rounded.Mail, "Mensajes", onMessages)
        HeaderIcon(Icons.Rounded.Notifications, "Notificaciones", onNotifications)
        HeaderIcon(Icons.Rounded.Menu, "Menu", onMenu)
    }
}

@Composable
private fun HeaderIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(icon, contentDescription = label, tint = WiCss.primary, modifier = Modifier.size(21.dp))
    }
}
