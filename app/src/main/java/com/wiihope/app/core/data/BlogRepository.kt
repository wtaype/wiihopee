package com.wiihope.app.core.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.wiihope.app.core.model.BlogPost
import kotlinx.coroutines.tasks.await

data class BlogPage(
    val posts: List<BlogPost>,
    val lastDoc: DocumentSnapshot?,
    val hasMore: Boolean,
)

class BlogRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun loadPage(
        limit: Long = 7,
        startAfter: DocumentSnapshot? = null,
        forceServer: Boolean = false,
    ): BlogPage {
        val query = firestore.collection("blog")
            .orderBy("pin", Query.Direction.DESCENDING)
            .orderBy("creado", Query.Direction.DESCENDING)
            .limit(limit + 1)
            .let { base -> if (startAfter == null) base else base.startAfter(startAfter) }

        val snap = query.fastGet(forceServer)
        val docs = snap.documents
        val visible = docs
            .map(BlogPost::fromFirestore)
            .filter { it.activo }
            .take(limit.toInt())

        return BlogPage(
            posts = visible,
            lastDoc = docs.take(limit.toInt()).lastOrNull(),
            hasMore = docs.size > limit,
        )
    }

    suspend fun loadPost(slug: String, forceServer: Boolean = false): BlogPost? {
        val docRef = firestore.collection("blog").document(slug)
        val snap = if (forceServer) {
            docRef.get(Source.SERVER).await()
        } else {
            runCatching { docRef.get(Source.CACHE).await() }
                .getOrElse { docRef.get(Source.DEFAULT).await() }
                .let { cached -> if (cached.exists()) cached else docRef.get(Source.DEFAULT).await() }
        }
        return snap.takeIf { it.exists() }?.let(BlogPost::fromFirestore)?.takeIf { it.activo }
    }

    suspend fun addLike(slug: String) {
        firestore.collection("blog").document(slug)
            .update("likes", FieldValue.increment(1))
            .await()
    }

    private suspend fun Query.fastGet(forceServer: Boolean) = if (forceServer) {
        get(Source.SERVER).await()
    } else {
        runCatching { get(Source.CACHE).await() }
            .getOrElse { get(Source.DEFAULT).await() }
            .let { cached -> if (cached.isEmpty) get(Source.DEFAULT).await() else cached }
    }
}
