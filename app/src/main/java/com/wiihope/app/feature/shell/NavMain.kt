package com.wiihope.app.feature.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

@Composable
internal fun NavMain(selectedPage: WiPage, onSelected: (WiPage) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(WiCss.white)
            .padding(horizontal = 8.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val icons = listOf(
            Icons.Rounded.VolunteerActivism,
            Icons.Rounded.Book,
            Icons.Rounded.Favorite,
            Icons.Rounded.Headphones,
            Icons.Rounded.Settings,
        )
        WiPage.mainPages.forEachIndexed { index, page ->
            NavMainItem(
                label = page.label,
                icon = icons[index],
                selected = selectedPage == page,
                onClick = { onSelected(page) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun NavMainItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) WiCss.gold.copy(alpha = 0.20f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(icon, contentDescription = label, tint = if (selected) WiCss.primary else WiCss.gray.copy(alpha = 0.72f), modifier = Modifier.size(18.dp))
        Text(label, style = WiText.tiny.copy(color = if (selected) WiCss.primary else WiCss.gray), maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
