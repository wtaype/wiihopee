package com.wiihope.app.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wiihope.app.R
import com.wiihope.app.feature.player.WiiHopeUiState
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.WiField
import com.wiihope.app.ui.components.WiGoldButton
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

private enum class AuthMode { Login, Register, Recover }

private fun cleanEmail(value: String): String =
    value.lowercase().filter { it.isLetterOrDigit() || it in "@._+-" }.take(120)

private fun cleanUser(value: String): String =
    value.lowercase().filter { it.isLetterOrDigit() || it == '_' || it == '-' }.take(32)

private fun cleanName(value: String): String =
    value.filter { it.isLetter() || it.isWhitespace() || it == '-' }.replace(Regex("\\s+"), " ").take(60)

private fun cleanLoginId(value: String): String =
    value.lowercase().filter { it.isLetterOrDigit() || it in "@._+-" }.take(120)

@Composable
internal fun AuthShell(
    state: WiiHopeUiState,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String, String, String, String, String) -> Unit,
    onRecover: (String) -> Unit,
    onGoogleClick: () -> Unit,
    onCompleteGoogle: (String, String, String) -> Unit,
) {
    var mode by remember { mutableStateOf(AuthMode.Login) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { AuthBrandCard() }
        item {
            if (state.googlePending) {
                GoogleCompleteCard(state.authLoading, state.googleEmail, onCompleteGoogle)
            } else {
                when (mode) {
                    AuthMode.Login -> LoginCard(state.authLoading, onLogin, onGoogleClick, onRegisterClick = { mode = AuthMode.Register }, onRecoverClick = { mode = AuthMode.Recover })
                    AuthMode.Register -> RegisterCard(state.authLoading, onRegister, onGoogleClick, onLoginClick = { mode = AuthMode.Login })
                    AuthMode.Recover -> RecoverCard(state.authLoading, onRecover, onBack = { mode = AuthMode.Login })
                }
            }
        }
    }
}

@Composable
private fun AuthBrandCard() {
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.75f) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(74.dp).clip(CircleShape))
            Column(Modifier.padding(start = 16.dp).weight(1f)) {
                GoldPill("WiiHope")
                Text("Fe, audio y esperanza", style = WiText.h1, modifier = Modifier.padding(top = 8.dp))
                Text("Una experiencia espiritual simple, luminosa y personal.", style = WiText.body, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
private fun GoogleButton(text: String, loading: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        enabled = !loading,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = WiCss.white.copy(alpha = 0.82f),
            contentColor = WiCss.text700,
            disabledContainerColor = WiCss.white.copy(alpha = 0.54f),
            disabledContentColor = WiCss.text300,
        ),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.2.dp, WiCss.goldSoft.copy(alpha = 0.65f)),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = WiCss.secondary)
        } else {
            Image(painterResource(R.drawable.ic_google_logo), contentDescription = null, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PasswordEye(visible: Boolean, onToggle: () -> Unit) {
    IconButton(onClick = onToggle) {
        Icon(
            if (visible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
            contentDescription = if (visible) "Ocultar contrasena" else "Ver contrasena",
            tint = WiCss.secondary,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun LoginCard(loading: Boolean, onLogin: (String, String) -> Unit, onGoogleClick: () -> Unit, onRegisterClick: () -> Unit, onRecoverClick: () -> Unit) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.58f) {
        Text("Iniciar sesion", style = WiText.h2)
        GoogleButton("Continuar con Google", loading, onGoogleClick, Modifier.fillMaxWidth().padding(top = 14.dp))
        Text("o usa tu email", style = WiText.tiny, modifier = Modifier.fillMaxWidth().padding(top = 12.dp), textAlign = TextAlign.Center)
        WiField(user, { user = cleanLoginId(it) }, "Email o usuario", leadingIcon = Icons.Rounded.Person, modifier = Modifier.padding(top = 14.dp))
        WiField(
            pass,
            { pass = it },
            "Contrasena",
            leadingIcon = Icons.Rounded.Lock,
            trailingIcon = { PasswordEye(showPass) { showPass = !showPass } },
            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.padding(top = 10.dp),
        )
        WiGoldButton("Entrar", { onLogin(user, pass) }, Modifier.fillMaxWidth().padding(top = 16.dp), Icons.AutoMirrored.Rounded.Login, loading)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onRecoverClick) { Text("Olvide mi contrasena") }
            TextButton(onClick = onRegisterClick) { Text("Crear cuenta") }
        }
    }
}

@Composable
private fun RegisterCard(loading: Boolean, onRegister: (String, String, String, String, String, String, String) -> Unit, onGoogleClick: () -> Unit, onLoginClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }
    var showPass by remember { mutableStateOf(false) }
    var showPass2 by remember { mutableStateOf(false) }
    GlassCard(Modifier.fillMaxWidth()) {
        Text("Registro", style = WiText.h2)
        GoogleButton("Registrarme con Google", loading, onGoogleClick, Modifier.fillMaxWidth().padding(top = 12.dp))
        Text("o crea tu acceso con email", style = WiText.tiny, modifier = Modifier.fillMaxWidth().padding(top = 12.dp), textAlign = TextAlign.Center)
        WiField(email, { email = cleanEmail(it) }, "Email", leadingIcon = Icons.Rounded.Mail, modifier = Modifier.padding(top = 12.dp))
        WiField(usuario, { usuario = cleanUser(it) }, "Usuario", leadingIcon = Icons.Rounded.Person, modifier = Modifier.padding(top = 10.dp))
        WiField(nombre, { nombre = cleanName(it) }, "Nombre", modifier = Modifier.padding(top = 10.dp))
        WiField(apellidos, { apellidos = cleanName(it) }, "Apellidos", modifier = Modifier.padding(top = 10.dp))
        WiField(
            pass,
            { pass = it },
            "Contrasena",
            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { PasswordEye(showPass) { showPass = !showPass } },
            leadingIcon = Icons.Rounded.Lock,
            modifier = Modifier.padding(top = 10.dp),
        )
        WiField(
            pass2,
            { pass2 = it },
            "Confirmar contrasena",
            visualTransformation = if (showPass2) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { PasswordEye(showPass2) { showPass2 = !showPass2 } },
            leadingIcon = Icons.Rounded.Lock,
            modifier = Modifier.padding(top = 10.dp),
        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp)) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it })
            Text("Acepto los terminos y condiciones", style = WiText.small)
        }
        WiGoldButton(
            "Crear cuenta",
            { if (accepted && pass == pass2) onRegister(email, usuario, nombre, apellidos, "", "masculino", pass) },
            Modifier.fillMaxWidth().padding(top = 16.dp),
            Icons.Rounded.Person,
            loading,
        )
        TextButton(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) { Text("Ya tengo cuenta", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
    }
}

@Composable
private fun GoogleCompleteCard(
    loading: Boolean,
    email: String,
    onComplete: (String, String, String) -> Unit,
) {
    var usuario by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }
    var showPass by remember { mutableStateOf(false) }
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.72f) {
        GoldPill("Google conectado")
        Text("Excelente, casi listo", style = WiText.h2, modifier = Modifier.padding(top = 10.dp))
        Text(email.ifBlank { "Completa tu cuenta de Google" }, style = WiText.body, modifier = Modifier.padding(top = 6.dp))
        Text("Crea tu usuario, una contrasena y acepta los terminos para guardar tu perfil en smiles.", style = WiText.small, modifier = Modifier.padding(top = 6.dp))
        WiField(usuario, { usuario = cleanUser(it) }, "Usuario", leadingIcon = Icons.Rounded.Person, modifier = Modifier.padding(top = 14.dp))
        WiField(
            pass,
            { pass = it },
            "Crea una contrasena",
            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { PasswordEye(showPass) { showPass = !showPass } },
            leadingIcon = Icons.Rounded.Lock,
            modifier = Modifier.padding(top = 10.dp),
        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp)) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it })
            Text("Acepto los terminos y condiciones", style = WiText.small)
        }
        WiGoldButton(
            if (loading) "Finalizando" else "Completar registro",
            { if (accepted) onComplete(usuario, pass, "smile") },
            Modifier.fillMaxWidth().padding(top = 12.dp),
            Icons.AutoMirrored.Rounded.Login,
            loading,
        )
    }
}

@Composable
private fun RecoverCard(loading: Boolean, onRecover: (String) -> Unit, onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    GlassCard(Modifier.fillMaxWidth()) {
        Text("Recuperar acceso", style = WiText.h2)
        Text("Te enviaremos un enlace para restablecer tu contrasena.", style = WiText.body, modifier = Modifier.padding(top = 8.dp))
        WiField(email, { email = cleanEmail(it) }, "Email", leadingIcon = Icons.Rounded.Mail, modifier = Modifier.padding(top = 12.dp))
        WiGoldButton("Enviar enlace", { onRecover(email) }, Modifier.fillMaxWidth().padding(top = 16.dp), Icons.Rounded.Mail, loading)
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
    }
}
