package com.wiihope.app

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.wiihope.app.feature.home.WiiHopeRoot
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.ui.theme.WiiHopeTheme

class MainActivity : ComponentActivity() {
    private var launchScreen by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_WiiHope)
        window.statusBarColor = Color.parseColor("#FFDA34")
        window.navigationBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        launchScreen = intent?.getStringExtra("screen")
        setContent {
            WiiHopeTheme {
                val viewModel: WiiHopeViewModel = viewModel()
                val googleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    runCatching {
                        GoogleSignIn.getSignedInAccountFromIntent(result.data).result
                    }.onSuccess { account ->
                        account.idToken?.let(viewModel::loginWithGoogleIdToken)
                            ?: viewModel.googleSignInFailed("Google no devolvio un token valido")
                    }.onFailure { error ->
                        val message = when (error) {
                            is ApiException -> {
                                val codeName = GoogleSignInStatusCodes.getStatusCodeString(error.statusCode)
                                "Google error ${error.statusCode} ($codeName)"
                            }
                            else -> error.localizedMessage ?: "Inicio con Google no disponible"
                        }
                        viewModel.googleSignInFailed(message)
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
                        client.signOut().addOnCompleteListener {
                            googleLauncher.launch(client.signInIntent)
                        }
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
