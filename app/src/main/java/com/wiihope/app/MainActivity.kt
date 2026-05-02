package com.wiihope.app

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.wiihope.app.feature.home.WiiHopeRoot
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.ui.theme.WiiHopeTheme

class MainActivity : ComponentActivity() {
    private var launchScreen by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchScreen = intent?.getStringExtra("screen")
        setContent {
            val notificationPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            WiiHopeTheme {
                val viewModel: WiiHopeViewModel = viewModel()
                val googleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    runCatching {
                        GoogleSignIn.getSignedInAccountFromIntent(result.data).result
                    }.onSuccess { account ->
                        account.idToken?.let(viewModel::loginWithGoogleIdToken)
                    }
                }
                WiiHopeRoot(
                    viewModel = viewModel,
                    launchScreen = launchScreen,
                    onGoogleClick = {
                        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build()
                        val client = GoogleSignIn.getClient(this, options)
                        googleLauncher.launch(client.signInIntent)
                    },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        launchScreen = intent.getStringExtra("screen")
    }
}
