package com.wiihope.app.feature.smiles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.components.WiInfoCard

@Composable
internal fun Mensajes() = SmilePage("Mensajes", "Centro de conversaciones y acompanamiento.", Icons.Rounded.Mail, "Pronto conectaremos respuestas, comunidad y avisos personales.")

@Composable
internal fun Notas() = SmilePage("Notas", "Ideas, apuntes y pensamientos guardados.", Icons.Rounded.StickyNote2, "Luego conectaremos una coleccion privada por usuario.")

@Composable
internal fun Planificar() = SmilePage("Planificar", "Herramientas para preparar contenido y devocionales.", Icons.Rounded.RocketLaunch, "Aqui creceran planes de lectura, borradores y agenda espiritual.")

@Composable
internal fun NuevoPost() = SmilePage("Nuevo Post", "Crear reflexiones y contenido para el blog.", Icons.Rounded.Add, "Reservado para editor nativo con titulo, categoria, portada y contenido.")

@Composable
private fun SmilePage(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, body: String) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero(title, subtitle, icon) }
        item { WiInfoCard("Proximo modulo", body) }
    }
}
