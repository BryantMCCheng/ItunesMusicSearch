package com.bryant.itunesmusicsearch

import com.bryant.itunesmusicsearch.apis.RetrofitService
import com.bryant.itunesmusicsearch.db.History
import com.bryant.itunesmusicsearch.db.HistoryRoomDataBase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber

class DataRepository(private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default) {

    private val dbManager by lazy {
        HistoryRoomDataBase.getInstance()
    }

    fun getHistory(): Flow<List<History>> {
        return dbManager.historyDao().getHistory()
    }

    suspend fun saveHistory(keyword: String) = withContext(defaultDispatcher) {
        dbManager.historyDao().insertHistory(History(keyword))
    }

    suspend fun getSearchInfo(input: String) = flow {
        emit(NetworkResult.Loading(true))
        Timber.d("flow, current thread = ${Thread.currentThread()}")
        val response = RetrofitService.searchApi.getSearchInfo(input)
        emit(NetworkResult.Success(response.results))
    }.flowOn(defaultDispatcher).catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
        Timber.d("catch, current thread = ${Thread.currentThread()}")
    }
}