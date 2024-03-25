package com.bryant.itunesmusicsearch.data.repository

import com.bryant.itunesmusicsearch.apis.SearchApi
import com.bryant.itunesmusicsearch.data.mapper.toDomainTrackList
import com.bryant.itunesmusicsearch.domain.model.ApiResult
import com.bryant.itunesmusicsearch.db.History
import com.bryant.itunesmusicsearch.db.HistoryDao
import com.bryant.itunesmusicsearch.domain.model.Track
import com.bryant.itunesmusicsearch.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the [SearchRepository] interface.
 * This class is responsible for coordinating data from remote and local data sources.
 */
@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val historyDao: HistoryDao
) : SearchRepository {

    override fun searchTracks(term: String): Flow<ApiResult<List<Track>>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = searchApi.getSearchInfo(term)
            if (response.isSuccessful) {
                val tracks = response.body()?.results?.toDomainTrackList() ?: emptyList()
                emit(ApiResult.Success(tracks))
            } else {
                emit(ApiResult.Error(Exception("API Error: ${response.code()} ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e))
        }
    }

    override fun getSearchHistory(): Flow<List<String>> {
        return historyDao.getHistory().map { historyList ->
            historyList.map { it.keyword }
        }
    }

    override suspend fun saveSearchTerm(term: String) {
        historyDao.insertHistory(History(term))
    }
}