package com.wiihope.app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.wiihope.app.R
import com.wiihope.app.core.model.AudioTrack

@Composable
fun TrackArtwork(
    track: AudioTrack?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes fallback: Int = R.drawable.album,
) {
    val fallbackRes = track?.artworkRes ?: fallback
    AsyncImage(
        model = track?.artworkUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = painterResource(fallbackRes),
        error = painterResource(fallbackRes),
        fallback = painterResource(fallbackRes),
    )
}
