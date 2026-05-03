package com.wiihope.app.feature.player

import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.BibleBook
import com.wiihope.app.core.model.BibleLike
import com.wiihope.app.core.model.MusicLike
import com.wiihope.app.core.model.Quote
import com.wiihope.app.core.model.UserProfile

data class WiiHopeUiState(
    val booting: Boolean = true,
    val hasAuthSession: Boolean = false,
    val profile: UserProfile? = null,
    val books: List<BibleBook> = emptyList(),
    val bibleLikes: List<BibleLike> = emptyList(),
    val musicLikes: List<MusicLike> = emptyList(),
    val music: List<AudioTrack> = emptyList(),
    val publicQuotes: List<Quote> = emptyList(),
    val privateQuotes: List<Quote> = emptyList(),
    val quotesTotal: Long = 0L,
    val quotesHasMore: Boolean = false,
    val musicLoading: Boolean = false,
    val musicLikesLoading: Boolean = false,
    val bibleLikesLoading: Boolean = false,
    val quotesLoading: Boolean = false,
    val quotesLoadingMore: Boolean = false,
    val authLoading: Boolean = false,
    val googlePending: Boolean = false,
    val googleEmail: String = "",
    val googleName: String = "",
    val message: String? = null,
    val error: String? = null,
) {
    val isLoggedIn: Boolean get() = profile != null || (hasAuthSession && !googlePending)
}
