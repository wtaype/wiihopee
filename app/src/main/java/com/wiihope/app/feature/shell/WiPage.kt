package com.wiihope.app.feature.shell

enum class WiPage(val label: String, val navIndex: Int? = null) {
    Oracion("Oracion", 0),
    Biblia("Biblia", 1),
    Citas("Citas", 2),
    Blog("Blog", 3),
    Ajustes("Ajustes", 4),
    Dashboard("Dashboard"),
    Musica("Musica"),
    Descubre("Descubre"),
    OraPorMi("Ora por mi"),
    ChatWil("ChatWil"),
    Acerca("Acerca"),
    Terminos("Terminos"),
    Privacidad("Privacidad"),
    Feedback("Feedback"),
    Contacto("Contacto"),
    Mensajes("Mensajes"),
    Notificaciones("Notificaciones"),
    Notas("Notas"),
    Planificar("Planificar"),
    NuevoPost("Nuevo Post"),
    Admin("Admin"),
    Usuarios("Usuarios"),
    Gestor("Gestor");

    val isMain: Boolean get() = navIndex != null

    companion object {
        val mainPages = listOf(Oracion, Biblia, Citas, Blog, Ajustes)

        fun fromLaunchScreen(value: String?): WiPage? = when (value) {
            "prayer" -> Oracion
            "bible" -> Biblia
            "quotes" -> Citas
            "music", "player" -> Musica
            "settings" -> Ajustes
            else -> null
        }
    }
}
