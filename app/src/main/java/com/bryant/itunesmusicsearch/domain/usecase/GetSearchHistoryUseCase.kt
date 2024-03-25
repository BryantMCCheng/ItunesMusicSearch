package com.bryant.itunesmusicsearch.domain.usecase

import com.bryant.itunesmusicsearch.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting the search history.
 */
class GetSearchHistoryUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return repository.getSearchHistory()
    }
}