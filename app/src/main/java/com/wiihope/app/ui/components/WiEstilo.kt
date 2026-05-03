package com.wiihope.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> Showi(target: T, modifier: Modifier = Modifier, content: @Composable (T) -> Unit) {
    AnimatedContent(
        targetState = target,
        modifier = modifier,
        transitionSpec = {
            (fadeIn(tween(240)) + slideInVertically(tween(260)) { it / 12 })
                .togetherWith(fadeOut(tween(160)))
        },
        label = "Showi",
    ) { page ->
        content(page)
    }
}

@Composable
fun MenuSlide(open: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = open,
        enter = fadeIn(tween(180)) + slideInHorizontally(tween(260)) { it },
        exit = fadeOut(tween(180)) + slideOutHorizontally(tween(220)) { it },
    ) {
        content()
    }
}

@Composable
fun WiHero(title: String, subtitle: String, icon: ImageVector, modifier: Modifier = Modifier) {
    GlassCard(modifier.fillMaxWidth(), intensity = 0.76f) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(WiCss.gold),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = WiCss.black, modifier = Modifier.size(24.dp))
            }
            Column(Modifier.padding(start = 14.dp).weight(1f)) {
                Text(title, style = WiText.h2)
                Text(subtitle, style = WiText.small, modifier = Modifier.padding(top = 3.dp))
            }
        }
    }
}

@Composable
fun WiSection(title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Text(title, style = WiText.h2)
        if (!subtitle.isNullOrBlank()) Text(subtitle, style = WiText.body, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun WiInfoCard(title: String, body: String, icon: ImageVector? = null, modifier: Modifier = Modifier) {
    GlassCard(modifier.fillMaxWidth(), intensity = 0.56f) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (icon != null) {
                Box(Modifier.size(38.dp).clip(CircleShape).background(WiCss.gold.copy(alpha = 0.20f)), contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = WiCss.primary, modifier = Modifier.size(18.dp))
                }
            }
            Column(Modifier.weight(1f)) {
                Text(title, style = WiText.h3)
                Text(body, style = WiText.body, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
fun WiLegalBlock(title: String, body: String, modifier: Modifier = Modifier) {
    GlassCard(modifier.fillMaxWidth(), intensity = 0.50f) {
        Text(title, style = WiText.h3)
        Text(body, style = WiText.body, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Start)
    }
}
