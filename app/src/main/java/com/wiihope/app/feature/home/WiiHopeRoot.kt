package com.wiihope.app.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.feature.shell.MainShell
import com.wiihope.app.ui.theme.premiumBackground

@Composable
fun WiiHopeRoot(viewModel: WiiHopeViewModel, launchScreen: String?, onGoogleClick: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val playback by viewModel.playbackState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.message, state.error) {
        val message = state.error ?: state.message
        if (message != null) {
            snackbar.showSnackbar(message)
            viewModel.consumeMessages()
        }
    }

    Box(Modifier.fillMaxSize().premiumBackground()) {
        when {
            state.booting -> SplashScreen()
            !state.isLoggedIn -> AuthShell(
                state = state,
                onLogin = viewModel::login,
                onRegister = viewModel::register,
                onRecover = viewModel::recover,
                onGoogleClick = onGoogleClick,
                onCompleteGoogle = viewModel::completeGoogleRegistration,
            )
            else -> MainShell(
                state = state,
                playback = playback,
                launchScreen = launchScreen,
                viewModel = viewModel,
                snackbar = snackbar,
            )
        }
        SnackbarHost(hostState = snackbar, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
