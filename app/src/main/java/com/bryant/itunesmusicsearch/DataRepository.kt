package com.bryant.itunesmusicsearch

import com.bryant.itunesmusicsearch.apis.RetrofitService
import com.bryant.itunesmusicsearch.db.History
import com.bryant.itunesmusicsearch.db.HistoryRoomDataBase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DataRepository(private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default) {

    private val dbManager by lazy {
        HistoryRoomDataBase.getInstance()
    }

    suspend fun getSearchInfo(input: String) = withContext(defaultDispatcher) {
        RetrofitService.searchApi.getSearchInfo(input)
    }

    fun getHistoryInfo(): Flow<List<History>> {
        return dbManager.historyDao().getHistory()
    }

    suspend fun saveHistory(keyword: String) = withContext(defaultDispatcher) {
        dbManager.historyDao().insertHistory(History(keyword))
    }
}