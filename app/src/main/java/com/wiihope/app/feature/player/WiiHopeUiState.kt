package com.wiihope.app.feature.player

import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.BibleBook
import com.wiihope.app.core.model.Quote
import com.wiihope.app.core.model.UserProfile

data class WiiHopeUiState(
    val booting: Boolean = true,
    val hasAuthSession: Boolean = false,
    val profile: UserProfile? = null,
    val books: List<BibleBook> = emptyList(),
    val music: List<AudioTrack> = emptyList(),
    val publicQuotes: List<Quote> = emptyList(),
    val privateQuotes: List<Quote> = emptyList(),
    val musicLoading: Boolean = false,
    val quotesLoading: Boolean = false,
    val authLoading: Boolean = false,
    val googlePending: Boolean = false,
    val googleEmail: String = "",
    val googleName: String = "",
    val message: String? = null,
    val error: String? = null,
) {
    val isLoggedIn: Boolean get() = profile != null || (hasAuthSession && !googlePending)
}
