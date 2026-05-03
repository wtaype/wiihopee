package com.wiihope.app.core.data

import com.wiihope.app.core.model.BibleBook

object BibleCatalog {
    private const val BaseUrl = "https://raw.githubusercontent.com/geluksee/hope/main"
    private val books = linkedMapOf(
        "San Mateo" to 28,
        "San Marcos" to 16,
        "San Lucas" to 24,
        "San Juan" to 21,
        "Hechos" to 28,
        "Romanos" to 16,
        "1 Corintios" to 16,
        "2 Corintios" to 13,
        "Galatas" to 6,
        "Efesios" to 6,
        "Filipenses" to 4,
        "Colosenses" to 4,
        "1 Tesalonicenses" to 5,
        "2 Tesalonicenses" to 3,
        "1 Timoteo" to 6,
        "2 Timoteo" to 4,
        "Tito" to 3,
        "Filemon" to 1,
        "Hebreos" to 13,
        "Santiago" to 5,
        "1 San Pedro" to 5,
        "2 San Pedro" to 3,
        "1 San Juan" to 5,
        "2 San Juan" to 1,
        "3 San Juan" to 1,
        "Judas" to 1,
        "Apocalipsis" to 22,
    )

    fun books(): List<BibleBook> {
        var track = 1
        return books.entries.mapIndexed { index, entry ->
            BibleBook(
                id = index + 1,
                name = entry.key,
                slug = entry.key.slug(),
                chapters = entry.value,
                baseUrl = BaseUrl,
                fileName = entry.key.replace(" ", "_"),
                trackStart = track,
            ).also { track += entry.value }
        }
    }

    private fun String.slug(): String =
        lowercase()
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ñ", "n")
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
}
