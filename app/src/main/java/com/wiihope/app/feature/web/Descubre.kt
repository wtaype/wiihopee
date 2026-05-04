package com.wiihope.app.feature.web

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wiihope.app.R
import com.wiihope.app.feature.shell.WiPage
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.WiInfoCard
import com.wiihope.app.ui.components.WiSection
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

private data class DiscoverCard(
    val title: String,
    val body: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

@Composable
internal fun Descubre(onNavigate: (WiPage) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Image(
                painter = painterResource(R.drawable.jesus),
                contentDescription = "Descubre WiiHope",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(30.dp)),
                contentScale = ContentScale.Crop,
            )
        }
        item {
            GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.80f) {
                GoldPill("Nuestro proposito")
                Text(
                    "Respira, vibra y ama",
                    style = WiText.display.copy(color = WiCss.primary),
                    modifier = Modifier.padding(top = 10.dp),
                )
                Text(
                    "Descubre todo lo que WiiHope tiene para ofrecerte. Un rincon digital disenado para renovar tu fe, encontrar paz y compartir luz con el mundo.",
                    style = WiText.body,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Start,
                )
            }
        }
        item { WiSection("Explora la experiencia", "Una vista mas editorial de lo que hace especial a WiiHope.") }
        items(
            listOf(
                DiscoverCard("Un refugio de oracion", "Encuentra consuelo, guias espirituales y un espacio para volver a centrarte.", Icons.Rounded.Spa),
                DiscoverCard("Reflexiones de vida", "Lee articulos y mensajes que alimentan el alma sin distracciones.", Icons.Rounded.AutoStories),
                DiscoverCard("Comunidad de fe", "Una experiencia hecha para compartir amor, esperanza y crecimiento espiritual.", Icons.Rounded.Groups),
                DiscoverCard("Audio y musica", "Biblia y player premium para acompanarte en tus momentos de calma.", Icons.Rounded.Headphones),
                DiscoverCard("Mensajes de esperanza", "Versiculos, citas y pequenas luces para tus mananas y tus noches.", Icons.Rounded.Campaign),
                DiscoverCard("Experiencia ligera", "Abrimos rapido, cuidamos tus datos y priorizamos una sensacion suave.", Icons.Rounded.Favorite),
            ),
        ) { card ->
            WiInfoCard(card.title, card.body, card.icon)
        }
        item {
            WiButton(
                text = "Ir al blog",
                onClick = { onNavigate(WiPage.Blog) },
                icon = Icons.Rounded.OpenInNew,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            WiButton(
                text = "Leer la Biblia",
                onClick = { onNavigate(WiPage.Biblia) },
                icon = Icons.Rounded.Explore,
                modifier = Modifier.fillMaxWidth(),
                outlined = true,
            )
        }
    }
}
