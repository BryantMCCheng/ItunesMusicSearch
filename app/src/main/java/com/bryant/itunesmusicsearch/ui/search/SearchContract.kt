package com.bryant.itunesmusicsearch.ui.search

import com.bryant.itunesmusicsearch.domain.model.Track

/**
 * Defines the MVI contract for the Search screen.
 */
class SearchContract {

    /**
     * Represents the state of the Search screen.
     */
    data class State(
        val tracks: List<Track> = emptyList(),
        val history: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val isHistoryVisible: Boolean = false,
        val error: String? = null,
        val notFoundKeyword: String? = null
    )

    /**
     * Represents the user's intents on the Search screen.
     */
    sealed interface Intent {
        data class Search(val term: String) : Intent
        data class OnSearchFocusChanged(val hasFocus: Boolean) : Intent
        data class OnTrackClicked(val track: Track) : Intent
        data class OnHistoryItemClicked(val term: String) : Intent
    }

    /**
     * Represents one-time effects on the Search screen.
     */
    sealed interface Effect {
        data class NavigateToPlayer(val track: Track) : Effect
        data object ScrollToTop : Effect
    }
}