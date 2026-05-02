package com.wiihope.app.core.data

import com.wiihope.app.core.model.Quote
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class QuoteRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun loadPublicQuotes(limit: Long = 100): List<Quote> {
        val snapshot = firestore.collection("wicitas")
            .orderBy("creado", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()
        return snapshot.documents.map(Quote::fromFirestore).filter { it.publico }
    }

    suspend fun loadPrivateQuotes(email: String, limit: Long = 100): List<Quote> {
        val snapshot = firestore.collection("wicitas")
            .orderBy("creado", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()
        return snapshot.documents.map(Quote::fromFirestore).filter { !it.publico && it.email == email }
    }

    suspend fun saveQuote(quote: Quote) {
        val collection = firestore.collection("wicitas")
        val doc = if (quote.id.isBlank()) collection.document() else collection.document(quote.id)
        doc.set(quote.copy(id = doc.id).toFirestore(isNew = quote.id.isBlank())).await()
    }

    suspend fun toggleFavorite(quote: Quote) {
        firestore.collection("wicitas").document(quote.id).update("favorito", !quote.favorito).await()
    }

    suspend fun deleteQuote(quote: Quote) {
        firestore.collection("wicitas").document(quote.id).delete().await()
    }
}
