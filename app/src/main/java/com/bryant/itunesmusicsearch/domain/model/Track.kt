package com.bryant.itunesmusicsearch.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a track in the domain layer. This is a clean, framework-agnostic model.
 */
@Parcelize
data class Track(
    val trackId: Long,
    val artistName: String,
    val trackName: String,
    val artworkUrl: String,
    val previewUrl: String
) : Parcelable