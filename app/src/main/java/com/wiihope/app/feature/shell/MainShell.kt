package com.wiihope.app.feature.shell

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wiihope.app.core.audio.PlaybackState
import com.wiihope.app.feature.pages.citas.QuoteFab
import com.wiihope.app.feature.player.WiiHopeUiState
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.ui.theme.premiumBackground
import kotlinx.coroutines.launch

@Composable
internal fun MainShell(
    state: WiiHopeUiState,
    playback: PlaybackState,
    launchScreen: String?,
    viewModel: WiiHopeViewModel,
    snackbar: SnackbarHostState,
) {
    val pagerState = rememberPagerState(pageCount = { WiPage.mainPages.size })
    val scope = rememberCoroutineScope()
    var currentPage by remember { mutableStateOf(WiPage.Oracion) }
    var lastMainPage by remember { mutableStateOf(WiPage.Oracion) }
    var menuOpen by remember { mutableStateOf(false) }

    fun openPage(page: WiPage) {
        currentPage = page
        if (page.isMain) {
            lastMainPage = page
            page.navIndex?.let { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
            }
        }
    }

    LaunchedEffect(launchScreen) {
        WiPage.fromLaunchScreen(launchScreen)?.let { openPage(it) }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (currentPage.isMain) {
            val page = WiPage.mainPages[pagerState.currentPage]
            currentPage = page
            lastMainPage = page
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Header(
                onHome = { openPage(WiPage.Oracion) },
                onMessages = { openPage(WiPage.Mensajes) },
                onNotifications = { openPage(WiPage.Notificaciones) },
                onMenu = { menuOpen = true },
            )
        },
        floatingActionButton = {
            if (currentPage == WiPage.Citas) QuoteFab(viewModel)
        },
        bottomBar = {
            Column {
                if (currentPage != WiPage.Oracion) {
                    MiniPlayer(
                        state = playback,
                        onToggle = viewModel::togglePlayback,
                        onPrevious = viewModel::previous,
                        onNext = viewModel::next,
                        onLoop = viewModel::toggleLoopOne,
                        onSeek = viewModel::seekTo,
                    )
                }
                NavMain(lastMainPage) { page -> openPage(page) }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).premiumBackground()) {
            if (currentPage.isMain) {
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
                    WiMain(WiPage.mainPages[index], state, playback, viewModel)
                }
            } else {
                WiMain(currentPage, state, playback, viewModel)
            }
        }
    }

    MenuRight(
        open = menuOpen,
        role = state.profile?.rol.orEmpty(),
        onClose = { menuOpen = false },
        onNavigate = { page ->
            menuOpen = false
            openPage(page)
        },
        onLogout = {
            menuOpen = false
            viewModel.logout()
        },
    )
}
