package com.wiihope.app.core.data

import com.wiihope.app.core.model.Quote
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

data class QuotePage(
    val quotes: List<Quote>,
    val lastDoc: DocumentSnapshot?,
    val hasMore: Boolean,
)

class QuoteRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun loadPublicQuotesPage(
        limit: Long = 7,
        startAfter: DocumentSnapshot? = null,
        forceServer: Boolean = false,
    ): QuotePage {
        val query = publicOrderedQuery(limit + 1).let { base ->
            if (startAfter == null) base else base.startAfter(startAfter)
        }
        val snapshot = query.fastGet(forceServer)
        val docs = snapshot.documents
        val visibleDocs = docs.take(limit.toInt())
        return QuotePage(
            quotes = visibleDocs.map(Quote::fromFirestore),
            lastDoc = visibleDocs.lastOrNull(),
            hasMore = docs.size > limit,
        )
    }

    suspend fun countPublicQuotes(): Long =
        firestore.collection("wicitas")
            .whereEqualTo("publico", true)
            .count()
            .get(AggregateSource.SERVER)
            .await()
            .count

    private fun publicOrderedQuery(limit: Long) =
        firestore.collection("wicitas")
            .whereEqualTo("publico", true)
            .orderBy("favorito", Query.Direction.DESCENDING)
            .orderBy("creado", Query.Direction.DESCENDING)
            .limit(limit)

    private suspend fun Query.fastGet(forceServer: Boolean) = if (forceServer) {
        get(Source.SERVER).await()
    } else {
        runCatching { get(Source.CACHE).await() }
            .getOrElse { get(Source.DEFAULT).await() }
            .let { cached -> if (cached.isEmpty) get(Source.DEFAULT).await() else cached }
    }

    suspend fun saveQuote(quote: Quote) {
        val collection = firestore.collection("wicitas")
        val doc = if (quote.id.isBlank()) collection.document("cita_${System.currentTimeMillis()}") else collection.document(quote.id)
        doc.set(quote.copy(id = doc.id).toFirestore(isNew = quote.id.isBlank())).await()
    }

    suspend fun toggleFavorite(quote: Quote) {
        firestore.collection("wicitas").document(quote.id).update("favorito", !quote.favorito).await()
    }

    suspend fun deleteQuote(quote: Quote) {
        firestore.collection("wicitas").document(quote.id).delete().await()
    }
}
