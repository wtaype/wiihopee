package com.wiihope.app.feature.web

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiihope.app.core.app.Wii
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.components.WiInfoCard
import com.wiihope.app.ui.components.WiLegalBlock

@Composable
internal fun OraPorMi() = SimpleInfoPage(
    title = "Ora por mi",
    subtitle = "Un espacio para escribir peticiones y recibir acompanamiento.",
    icon = Icons.Rounded.ChatBubble,
    cards = listOf(
        "Peticion de oracion" to "Preparado para formulario privado con motivo, mensaje y seguimiento.",
        "Acompanamiento" to "Luego podremos conectar respuestas, mensajes y notificaciones espirituales.",
    ),
)

@Composable
internal fun ChatWil() = SimpleInfoPage(
    title = "ChatWil",
    subtitle = "Acompanamiento conversacional para momentos importantes.",
    icon = Icons.Rounded.ChatBubble,
    cards = listOf("Memoria espiritual" to "Este modulo podra crecer con historial, contexto y mensajes seguros."),
)

@Composable
internal fun Acerca() = SimpleInfoPage(
    title = "Acerca de WiiHope",
    subtitle = "Una app de fe creada con amor.",
    icon = Icons.Rounded.Info,
    cards = listOf(Wii.app to "${Wii.desc}. Version ${Wii.version}.", "Creado por" to "${Wii.by} · ${Wii.link}"),
)

@Composable
internal fun Privacidad() {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero("Privacidad", "Tu informacion debe estar protegida y clara.", Icons.Rounded.PrivacyTip) }
        item { WiLegalBlock("Datos principales", "Usamos Firebase Auth y Firestore para guardar datos necesarios: perfil, citas y preferencias esenciales.") }
        item { WiLegalBlock("Control", "Puedes cerrar sesion y actualizar informacion desde Ajustes. Mas adelante agregaremos controles avanzados de privacidad.") }
    }
}

@Composable
internal fun Feedback() = SimpleInfoPage(
    title = "Feedback",
    subtitle = "Tus ideas ayudan a mejorar WiiHope.",
    icon = Icons.Rounded.Feedback,
    cards = listOf("Sugerencias" to "Preparado para reportes, propuestas y comentarios desde la app."),
)

@Composable
internal fun Contacto() = SimpleInfoPage(
    title = "Contacto",
    subtitle = "Canales para hablar con el equipo.",
    icon = Icons.Rounded.ContactSupport,
    cards = listOf("Soporte" to "Pronto agregaremos email, redes y formulario directo."),
)

@Composable
private fun SimpleInfoPage(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, cards: List<Pair<String, String>>) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero(title, subtitle, icon) }
        cards.forEach { (cardTitle, cardBody) ->
            item { WiInfoCard(cardTitle, cardBody) }
        }
    }
}
