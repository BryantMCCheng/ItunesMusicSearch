package com.bryant.itunesmusicsearch.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    val imageUrl: String?, // "https://is3-ssl.mzstatic.com/image/thumb/Music125/v4/79/1e/dc/791edcf0-73ab-96e7-b6c4-29943a79c14c/14UMGIM27067.rgb.jpg/100x100bb.jpg"
    val trackName: String?, // "Moves Like (feat. Christina Aguilera)"
    val artistName: String?, // "Maroon 5"
    val previewUrl: String? // "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/12/d3/24/12d3248d-8b3b-53fd-1249-3bfb1834d0a0/mzaf_17675713238865539816.plus.aac.p.m4a"
) : Parcelable