package com.bryant.itunesmusicsearch.domain.usecase

import com.bryant.itunesmusicsearch.domain.model.ApiResult
import com.bryant.itunesmusicsearch.domain.model.Track
import com.bryant.itunesmusicsearch.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for searching tracks. This encapsulates a single business operation.
 */
class GetSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(term: String): Flow<ApiResult<List<Track>>> {
        return repository.searchTracks(term)
    }
}