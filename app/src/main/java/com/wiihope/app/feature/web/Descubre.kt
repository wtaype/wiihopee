package com.wiihope.app.feature.web

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wiihope.app.R
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.components.WiInfoCard
import com.wiihope.app.ui.components.WiSection
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

@Composable
internal fun Descubre() {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Image(
                painterResource(R.drawable.jesus),
                contentDescription = "Descubre WiiHope",
                modifier = Modifier.fillMaxWidth().height(240.dp).clip(RoundedCornerShape(28.dp)),
                contentScale = ContentScale.Crop,
            )
        }
        item {
            Column {
                GoldPill("WiiHope")
                Text("Una experiencia de fe, audio y esperanza", style = WiText.display.copy(color = WiCss.primary), modifier = Modifier.padding(top = 10.dp))
                Text(
                    "Ora, escucha la Biblia, guarda citas y acompana tu dia con una app nativa pensada para sentirse ligera, luminosa y cercana.",
                    style = WiText.body,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
        item { WiHero("Todo en un solo lugar", "Una base espiritual para cada dia.", Icons.Rounded.Explore) }
        item { WiSection("Modulos destacados", "Cada espacio esta conectado al mismo corazon: esperanza simple y constante.") }
        item { WiInfoCard("Oracion diaria", "Padre Nuestro con audio suave y una entrada visual para iniciar el dia.", Icons.Rounded.VolunteerActivism) }
        item { WiInfoCard("Audio Biblia", "Nuevo Testamento por libros y capitulos, listo para escuchar sin distracciones.", Icons.Rounded.Book) }
        item { WiInfoCard("Musica", "Player persistente estilo premium para canciones cristianas y momentos de paz.", Icons.Rounded.Headphones) }
        item { WiInfoCard("Citas", "Promesas publicas, privadas y favoritas sincronizadas con Firestore.", Icons.Rounded.Favorite) }
    }
}
