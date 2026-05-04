package com.wiihope.app.feature.smiles

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.WiField
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.components.WiInfoCard
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText
import org.json.JSONArray
import org.json.JSONObject

private const val PREFS = "wiihope_local_notes"
private const val KEY_NOTES = "notes"
private const val KEY_DOCS = "docs"

private data class LocalItem(val id: String, val title: String, val body: String)

@Composable
internal fun Mensajes() = SmilePage("Mensajes", "Centro de conversaciones y acompanamiento.", Icons.Rounded.Mail, "Pronto conectaremos respuestas, comunidad y avisos personales.")

@Composable
internal fun Notas() {
    val context = LocalContext.current
    var notes by remember { mutableStateOf(loadItems(context, KEY_NOTES)) }
    var text by remember { mutableStateOf("") }

    fun persist(next: List<LocalItem>) {
        notes = next
        saveItems(context, KEY_NOTES, next)
    }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero("Notas", "Tarjetas pequenas guardadas solo en este celular.", Icons.Rounded.StickyNote2) }
        item {
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.58f) {
                WiField(text, { text = it }, "Nueva nota", singleLine = false, minLines = 2)
                WiButton(
                    "Guardar nota",
                    onClick = {
                        val clean = text.trim()
                        if (clean.isNotBlank()) {
                            persist(listOf(LocalItem(System.currentTimeMillis().toString(), "Nota", clean)) + notes)
                            text = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    icon = Icons.Rounded.Add,
                )
            }
        }
        if (notes.isEmpty()) {
            item { WiInfoCard("Sin notas todavia", "Escribe ideas, frases o recordatorios rapidos. Se guardan localmente.") }
        }
        items(notes, key = { it.id }) { note ->
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.50f) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(note.body, style = WiText.body, modifier = Modifier.weight(1f))
                    IconButton(onClick = { persist(notes.filterNot { it.id == note.id }) }) {
                        Icon(Icons.Rounded.Delete, "Eliminar", tint = WiCss.error)
                    }
                }
            }
        }
    }
}

@Composable
internal fun Planificar() {
    val context = LocalContext.current
    var docs by remember { mutableStateOf(loadItems(context, KEY_DOCS)) }
    var editor by remember { mutableStateOf<LocalItem?>(null) }

    fun persist(next: List<LocalItem>) {
        docs = next
        saveItems(context, KEY_DOCS, next)
    }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero("Planificar", "Documentos locales tipo borrador.", Icons.Rounded.RocketLaunch) }
        item {
            WiButton(
                "Nuevo documento",
                onClick = { editor = LocalItem("", "", "") },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                icon = Icons.Rounded.Add,
            )
        }
        if (docs.isEmpty()) {
            item { WiInfoCard("Sin documentos", "Crea planes, borradores, ideas de blog o devocionales. Se guardan solo en este celular.") }
        }
        items(docs, key = { it.id }) { doc ->
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.52f, onClick = { editor = doc }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(doc.title.ifBlank { "Sin titulo" }, style = WiText.h3, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(doc.body, style = WiText.small, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    IconButton(onClick = { persist(docs.filterNot { it.id == doc.id }) }) {
                        Icon(Icons.Rounded.Delete, "Eliminar", tint = WiCss.error)
                    }
                }
            }
        }
    }

    editor?.let { current ->
        PlanEditor(
            item = current,
            onDismiss = { editor = null },
            onSave = { saved ->
                val id = saved.id.ifBlank { System.currentTimeMillis().toString() }
                val next = listOf(saved.copy(id = id)) + docs.filterNot { it.id == id }
                persist(next)
                editor = null
            },
        )
    }
}

@Composable
private fun PlanEditor(item: LocalItem, onDismiss: () -> Unit, onSave: (LocalItem) -> Unit) {
    var title by remember(item.id) { mutableStateOf(item.title) }
    var body by remember(item.id) { mutableStateOf(item.body) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item.id.isBlank()) "Nuevo documento" else "Editar documento", style = WiText.h3) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                WiField(title, { title = it }, "Titulo")
                WiField(body, { body = it }, "Contenido", singleLine = false, minLines = 7)
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(item.copy(title = title.trim(), body = body.trim())) }) {
                Icon(Icons.Rounded.Save, null)
                Spacer(Modifier.height(0.dp))
                Text("Guardar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

@Composable
internal fun NuevoPost() = SmilePage("Nuevo Post", "Crear reflexiones y contenido para el blog.", Icons.Rounded.Add, "Reservado para editor nativo con titulo, categoria, portada y contenido.")

@Composable
private fun SmilePage(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, body: String) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero(title, subtitle, icon) }
        item { WiInfoCard("Proximo modulo", body) }
    }
}

private fun loadItems(context: Context, key: String): List<LocalItem> {
    val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(key, "[]").orEmpty()
    return runCatching {
        val arr = JSONArray(raw)
        List(arr.length()) { index ->
            val obj = arr.getJSONObject(index)
            LocalItem(
                id = obj.optString("id"),
                title = obj.optString("title"),
                body = obj.optString("body"),
            )
        }
    }.getOrDefault(emptyList())
}

private fun saveItems(context: Context, key: String, items: List<LocalItem>) {
    val arr = JSONArray()
    items.forEach {
        arr.put(JSONObject().put("id", it.id).put("title", it.title).put("body", it.body))
    }
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        .edit()
        .putString(key, arr.toString())
        .apply()
}
