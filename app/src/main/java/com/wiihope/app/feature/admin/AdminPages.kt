package com.wiihope.app.feature.admin

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
internal fun Admin() = AdminInfo("Admin", "Panel principal de plataforma.", "Aqui creceran metricas, gestion y herramientas internas.")

@Composable
internal fun Usuarios() = AdminInfo("Usuarios", "Gestion de comunidad WiiHope.", "Preparado para busqueda, roles, estado y soporte de usuarios.")

@Composable
private fun AdminInfo(title: String, subtitle: String, body: String) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero(title, subtitle, Icons.Rounded.Dashboard) }
        item { WiInfoCard("Modulo administrativo", body) }
    }
}
