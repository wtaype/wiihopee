package com.wiihope.app.feature.shell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wiihope.app.R
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

@Composable
internal fun MenuRight(
    open: Boolean,
    role: String,
    onClose: () -> Unit,
    onNavigate: (WiPage) -> Unit,
    onLogout: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = open,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(180)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WiCss.black.copy(alpha = 0.18f))
                    .clickable(onClick = onClose),
            )
        }
        AnimatedVisibility(
            visible = open,
            modifier = Modifier.align(Alignment.CenterEnd),
            enter = fadeIn(tween(180)) + slideInHorizontally(tween(260)) { fullWidth -> fullWidth },
            exit = fadeOut(tween(180)) + slideOutHorizontally(tween(220)) { fullWidth -> fullWidth },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.80f)
                    .background(WiCss.cream),
            ) {
                MenuContent(role, onClose, onNavigate, onLogout)
            }
        }
    }
}

@Composable
private fun MenuContent(role: String, onClose: () -> Unit, onNavigate: (WiPage) -> Unit, onLogout: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(48.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                Column(Modifier.padding(start = 12.dp).weight(1f)) {
                    Text("Descubre Ideas", style = WiText.h2)
                    Text("Wilder", style = WiText.small)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Rounded.Close, contentDescription = "Cerrar", tint = WiCss.primary, modifier = Modifier.size(22.dp))
                }
            }
        }
        item { MenuSection("Tu cuenta") }
        items(WiRoutes.roleRoutes(role).distinctBy { it.page }) { item -> MenuRoute(item, onNavigate) }
        item { MenuSection("Acerca") }
        items(WiRoutes.about) { item -> MenuRoute(item, onNavigate) }
        item {
            WiButton("Cerrar sesion", onLogout, Modifier.fillMaxWidth().padding(top = 4.dp), Icons.AutoMirrored.Rounded.Logout, color = WiCss.error, outlined = true)
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun MenuSection(title: String) {
    Text(title.uppercase(), style = WiText.label.copy(color = WiCss.secondary), modifier = Modifier.padding(top = 8.dp, start = 4.dp))
}

@Composable
private fun MenuRoute(route: WiRoute, onNavigate: (WiPage) -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.50f, onClick = { onNavigate(route.page) }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(42.dp).clip(CircleShape).background(WiCss.gold.copy(alpha = 0.20f)), contentAlignment = Alignment.Center) {
                Icon(route.icon, null, tint = WiCss.primary, modifier = Modifier.size(19.dp))
            }
            Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Text(route.title, style = WiText.h3)
                Text(route.subtitle, style = WiText.tiny, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = WiCss.gray, modifier = Modifier.size(20.dp))
        }
    }
}
