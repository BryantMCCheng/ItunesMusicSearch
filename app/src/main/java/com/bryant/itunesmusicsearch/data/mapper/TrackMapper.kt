package com.bryant.itunesmusicsearch.data.mapper

import com.bryant.itunesmusicsearch.data.remote.dto.TrackDto
import com.bryant.itunesmusicsearch.domain.model.Track

/**
 * Maps a [TrackDto] from the data layer to a [Track] in the domain layer.
 */
fun TrackDto.toDomainTrack(): Track {
    return Track(
        trackId = this.trackId ?: 0L,
        artistName = this.artistName ?: "Unknown Artist",
        trackName = this.trackName ?: "Unknown Track",
        artworkUrl = this.artworkUrl100 ?: this.artworkUrl60 ?: this.artworkUrl30 ?: "",
        previewUrl = this.previewUrl ?: ""
    )
}

/**
 * Maps a list of [TrackDto] from the data layer to a list of [Track] in the domain layer.
 */
fun List<TrackDto>.toDomainTrackList(): List<Track> {
    return this.map { it.toDomainTrack() }
}