package com.wiihope.app.feature.shell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiihope.app.core.audio.PlaybackState
import com.wiihope.app.feature.admin.Admin
import com.wiihope.app.feature.admin.Usuarios
import com.wiihope.app.feature.gestor.Gestor
import com.wiihope.app.feature.pages.ajustes.Ajustes
import com.wiihope.app.feature.pages.biblia.Biblia
import com.wiihope.app.feature.pages.citas.Citas
import com.wiihope.app.feature.pages.musica.Musica
import com.wiihope.app.feature.pages.oracion.Oracion
import com.wiihope.app.feature.player.WiiHopeUiState
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.feature.smiles.Mensajes
import com.wiihope.app.feature.smiles.Notas
import com.wiihope.app.feature.smiles.NuevoPost
import com.wiihope.app.feature.smiles.Planificar
import com.wiihope.app.feature.web.Acerca
import com.wiihope.app.feature.web.Blog
import com.wiihope.app.feature.web.ChatWil
import com.wiihope.app.feature.web.Contacto
import com.wiihope.app.feature.web.Descubre
import com.wiihope.app.feature.web.Feedback
import com.wiihope.app.feature.web.OraPorMi
import com.wiihope.app.feature.web.Privacidad
import com.wiihope.app.feature.web.Terminos
import com.wiihope.app.ui.components.Showi
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.components.WiInfoCard

@Composable
internal fun WiMain(page: WiPage, state: WiiHopeUiState, playback: PlaybackState, viewModel: WiiHopeViewModel) {
    Showi(target = page, modifier = Modifier.fillMaxSize()) { current ->
        when (current) {
            WiPage.Oracion -> Oracion(state.profile?.nombre.orEmpty(), playback, viewModel::play, viewModel::togglePlayback)
            WiPage.Biblia -> Biblia(state.books, playback, viewModel::play, viewModel::togglePlayback)
            WiPage.Citas -> Citas(state.publicQuotes, state.privateQuotes, state.quotesLoading, viewModel, onRefresh = { viewModel.refreshQuotes(forceServer = true) })
            WiPage.Musica -> Musica(
                tracks = state.music,
                loading = state.musicLoading,
                playback = playback,
                onRefresh = viewModel::refreshMusic,
                onPlay = viewModel::play,
                onToggle = viewModel::togglePlayback,
                onPrevious = viewModel::previous,
                onNext = viewModel::next,
            )
            WiPage.Ajustes -> Ajustes(state, viewModel)
            WiPage.Descubre -> Descubre()
            WiPage.Blog -> Blog()
            WiPage.OraPorMi -> OraPorMi()
            WiPage.ChatWil -> ChatWil()
            WiPage.Acerca -> Acerca()
            WiPage.Terminos -> Terminos()
            WiPage.Privacidad -> Privacidad()
            WiPage.Feedback -> Feedback()
            WiPage.Contacto -> Contacto()
            WiPage.Mensajes -> Mensajes()
            WiPage.Notificaciones -> Notificaciones()
            WiPage.Notas -> Notas()
            WiPage.Planificar -> Planificar()
            WiPage.NuevoPost -> NuevoPost()
            WiPage.Admin -> Admin()
            WiPage.Usuarios -> Usuarios()
            WiPage.Gestor -> Gestor()
        }
    }
}

@Composable
private fun Notificaciones() {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero("Notificaciones", "Recordatorios y novedades importantes.", Icons.Rounded.Notifications) }
        item { WiInfoCard("FCM listo", "La base ya puede recibir payloads para abrir Biblia, Citas, Musica o Ajustes.") }
    }
}
