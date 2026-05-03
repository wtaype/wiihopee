package com.wiihope.app.feature.gestor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.components.WiInfoCard

@Composable
internal fun Gestor() {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero("Gestor", "Resumen de actividad y acompanamiento.", Icons.Rounded.Dashboard) }
        item { WiInfoCard("Modulo gestor", "Preparado para mensajes, contenido y seguimiento de comunidad.") }
    }
}
