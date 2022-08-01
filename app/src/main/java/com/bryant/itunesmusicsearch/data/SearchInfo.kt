package com.bryant.itunesmusicsearch.data

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<ResultsItem>
)

data class ResultsItem(
    @SerializedName("artistId")
    val artistId: Int,
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("artistViewUrl")
    val artistViewUrl: String,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String,
    @SerializedName("artworkUrl30")
    val artworkUrl30: String,
    @SerializedName("artworkUrl60")
    val artworkUrl60: String,
    @SerializedName("collectionArtistName")
    val collectionArtistName: String,
    @SerializedName("collectionCensoredName")
    val collectionCensoredName: String,
    @SerializedName("collectionExplicitness")
    val collectionExplicitness: String,
    @SerializedName("collectionId")
    val collectionId: Int,
    @SerializedName("collectionName")
    val collectionName: String,
    @SerializedName("collectionPrice")
    val collectionPrice: Double,
    @SerializedName("collectionViewUrl")
    val collectionViewUrl: String,
    @SerializedName("contentAdvisoryRating")
    val contentAdvisoryRating: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("discCount")
    val discCount: Int,
    @SerializedName("discNumber")
    val discNumber: Int,
    @SerializedName("isStreamable")
    val isStreamable: Boolean,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("previewUrl")
    val previewUrl: String,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("trackCensoredName")
    val trackCensoredName: String,
    @SerializedName("trackCount")
    val trackCount: Int,
    @SerializedName("trackExplicitness")
    val trackExplicitness: String,
    @SerializedName("trackId")
    val trackId: Int,
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("trackNumber")
    val trackNumber: Int,
    @SerializedName("trackPrice")
    val trackPrice: Double,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Int,
    @SerializedName("trackViewUrl")
    val trackViewUrl: String,
    @SerializedName("wrapperType")
    val wrapperType: String
)
/*
{
    "wrapperType": "track",
    "kind": "song",
    "artistId": 1798556,
    "collectionId": 1440858689,
    "trackId": 1440859510,
    "artistName": "Maroon 5",
    "collectionName": "Hands All Over (Deluxe Edition)",
    "trackName": "Moves Like Jagger (feat. Christina Aguilera)",
    "collectionCensoredName": "Hands All Over (Deluxe Edition)",
    "trackCensoredName": "Moves Like Jagger (feat. Christina Aguilera)",
    "artistViewUrl": "https://music.apple.com/us/artist/maroon-5/1798556?uo=4",
    "collectionViewUrl": "https://music.apple.com/us/album/moves-like-jagger-feat-christina-aguilera/1440858689?i=1440859510&uo=4",
    "trackViewUrl": "https://music.apple.com/us/album/moves-like-jagger-feat-christina-aguilera/1440858689?i=1440859510&uo=4",
    "previewUrl": "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/12/d3/24/12d3248d-8b3b-53fd-1249-3bfb1834d0a0/mzaf_17675713238865539816.plus.aac.p.m4a",
    "artworkUrl30": "https://is3-ssl.mzstatic.com/image/thumb/Music125/v4/79/1e/dc/791edcf0-73ab-96e7-b6c4-29943a79c14c/14UMGIM27067.rgb.jpg/30x30bb.jpg",
    "artworkUrl60": "https://is3-ssl.mzstatic.com/image/thumb/Music125/v4/79/1e/dc/791edcf0-73ab-96e7-b6c4-29943a79c14c/14UMGIM27067.rgb.jpg/60x60bb.jpg",
    "artworkUrl100": "https://is3-ssl.mzstatic.com/image/thumb/Music125/v4/79/1e/dc/791edcf0-73ab-96e7-b6c4-29943a79c14c/14UMGIM27067.rgb.jpg/100x100bb.jpg",
    "collectionPrice": 11.99,
    "trackPrice": 1.29,
    "releaseDate": "2010-08-18T12:00:00Z",
    "collectionExplicitness": "notExplicit",
    "trackExplicitness": "notExplicit",
    "discCount": 1,
    "discNumber": 1,
    "trackCount": 19,
    "trackNumber": 13,
    "trackTimeMillis": 201240,
    "country": "USA",
    "currency": "USD",
    "primaryGenreName": "Pop",
    "isStreamable": true
}
*/