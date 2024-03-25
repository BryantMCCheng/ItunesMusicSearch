package com.bryant.itunesmusicsearch.domain.usecase

import com.bryant.itunesmusicsearch.domain.repository.SearchRepository
import javax.inject.Inject

/**
 * Use case for saving a search term to the history.
 */
class SaveSearchTermUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(term: String) {
        repository.saveSearchTerm(term)
    }
}