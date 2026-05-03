package com.wiihope.app.feature.web

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.components.WiLegalBlock

@Composable
internal fun Terminos() {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero("Terminos y condiciones", "Uso claro, respetuoso y seguro de WiiHope.", Icons.Rounded.Description) }
        item { WiLegalBlock("1. Uso de la app", "WiiHope ofrece contenido espiritual, audio, citas, musica y herramientas de acompanamiento. El usuario acepta usar la app con respeto y sin publicar contenido ofensivo, ilegal o que afecte a otras personas.") }
        item { WiLegalBlock("2. Cuenta y seguridad", "Cada usuario es responsable de mantener protegida su cuenta, correo y contrasena. Si detectas actividad sospechosa, recomendamos cerrar sesion y recuperar el acceso desde Firebase Auth.") }
        item { WiLegalBlock("3. Contenido espiritual", "Las oraciones, citas, reflexiones y audios tienen fines de inspiracion y acompanamiento. No reemplazan atencion medica, psicologica, legal o profesional cuando sea necesaria.") }
        item { WiLegalBlock("4. Datos y sincronizacion", "WiiHope usa Firebase para autenticar usuarios, guardar perfil, citas y preferencias esenciales. Buscamos mantener una experiencia rapida, privada y segura.") }
        item { WiLegalBlock("5. Cambios futuros", "La app puede mejorar funciones, diseno y politicas con el tiempo. Las pantallas legales dentro de la app se actualizaran cuando sea necesario.") }
    }
}
