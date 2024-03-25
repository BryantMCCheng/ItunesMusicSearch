package com.bryant.itunesmusicsearch.domain.repository

import com.bryant.itunesmusicsearch.domain.model.ApiResult
import com.bryant.itunesmusicsearch.domain.model.Track
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the search repository.
 * This defines the contract that the data layer must implement.
 */
interface SearchRepository {

    /**
     * Searches for tracks based on a given search term.
     * @param term The search term.
     * @return A Flow that emits the result of the API call.
     */
    fun searchTracks(term: String): Flow<ApiResult<List<Track>>>

    /**
     * Gets the search history.
     * @return A Flow that emits the list of search history keywords.
     */
    fun getSearchHistory(): Flow<List<String>>

    /**
     * Saves a search term to the history.
     * @param term The search term to save.
     */
    suspend fun saveSearchTerm(term: String)
}