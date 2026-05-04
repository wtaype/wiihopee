package com.wiihope.app.feature.shell

import androidx.activity.compose.BackHandler
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
    var blogPostOpen by remember { mutableStateOf(false) }
    var blogBackSignal by remember { mutableStateOf(0) }
    var ajustesSubPageOpen by remember { mutableStateOf(false) }
    var ajustesBackSignal by remember { mutableStateOf(0) }
    var pageBackStack by remember { mutableStateOf<List<WiPage>>(emptyList()) }

    fun openPage(page: WiPage, addToHistory: Boolean = true) {
        if (page == currentPage) return
        if (addToHistory && !page.isMain) {
            pageBackStack = (pageBackStack + currentPage).takeLast(8)
        }
        if (page.isMain) {
            pageBackStack = emptyList()
        }
        currentPage = page
        if (page.isMain) {
            lastMainPage = page
            page.navIndex?.let { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
            }
        }
    }

    fun goBack() {
        when {
            currentPage == WiPage.Blog && blogPostOpen -> blogBackSignal++
            currentPage == WiPage.Ajustes && ajustesSubPageOpen -> ajustesBackSignal++
            pageBackStack.isNotEmpty() -> {
                val previous = pageBackStack.last()
                pageBackStack = pageBackStack.dropLast(1)
                openPage(previous, addToHistory = false)
            }
            !currentPage.isMain -> openPage(lastMainPage, addToHistory = false)
        }
    }

    val canGoBack = (currentPage == WiPage.Blog && blogPostOpen) ||
        (currentPage == WiPage.Ajustes && ajustesSubPageOpen) ||
        !currentPage.isMain

    LaunchedEffect(launchScreen) {
        WiPage.fromLaunchScreen(launchScreen)?.let { openPage(it, addToHistory = false) }
    }

    LaunchedEffect(currentPage) {
        if (currentPage != WiPage.Blog) blogPostOpen = false
        if (currentPage != WiPage.Ajustes) ajustesSubPageOpen = false
    }

    LaunchedEffect(pagerState.currentPage) {
        if (currentPage.isMain) {
            val page = WiPage.mainPages[pagerState.currentPage]
            currentPage = page
            lastMainPage = page
            pageBackStack = emptyList()
        }
    }

    BackHandler(enabled = canGoBack) { goBack() }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Header(
                onHome = { openPage(WiPage.Oracion) },
                onMessages = { openPage(WiPage.Mensajes) },
                onNotifications = { openPage(WiPage.Notificaciones) },
                onMenu = { menuOpen = true },
                showBack = canGoBack,
                onBack = ::goBack,
            )
        },
        floatingActionButton = {
            val showingCitas = currentPage == WiPage.Citas &&
                (pagerState.currentPage == WiPage.Citas.navIndex || pagerState.targetPage == WiPage.Citas.navIndex)
            if (showingCitas) QuoteFab(viewModel)
        },
        bottomBar = {
            Column {
                if (currentPage != WiPage.Oracion) {
                    MiniPlayer(
                        state = playback,
                        onOpen = { source ->
                            when (source) {
                                com.wiihope.app.core.model.TrackSource.Prayer -> openPage(WiPage.Oracion)
                                com.wiihope.app.core.model.TrackSource.Bible -> openPage(WiPage.Biblia)
                                com.wiihope.app.core.model.TrackSource.Music -> openPage(WiPage.Musica)
                            }
                        },
                        onToggle = viewModel::togglePlayback,
                        onPrevious = viewModel::previous,
                        onNext = viewModel::next,
                        onLoop = viewModel::toggleLoopOne,
                        onSeek = viewModel::seekTo,
                    )
                }
                NavMain(lastMainPage) { page -> openPage(page, addToHistory = false) }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).premiumBackground()) {
            if (currentPage.isMain) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = !(currentPage == WiPage.Blog && blogPostOpen),
                ) { index ->
                    WiMain(
                        WiPage.mainPages[index],
                        state,
                        playback,
                        viewModel,
                        blogBackSignal = blogBackSignal,
                        onBlogPostOpenChange = { blogPostOpen = it },
                        ajustesBackSignal = ajustesBackSignal,
                        onAjustesSubPageOpenChange = { ajustesSubPageOpen = it },
                        onNavigate = { page -> openPage(page) },
                    )
                }
            } else {
                WiMain(
                    currentPage,
                    state,
                    playback,
                    viewModel,
                    blogBackSignal = blogBackSignal,
                    onBlogPostOpenChange = { blogPostOpen = it },
                    ajustesBackSignal = ajustesBackSignal,
                    onAjustesSubPageOpenChange = { ajustesSubPageOpen = it },
                    onNavigate = { page -> openPage(page) },
                )
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
