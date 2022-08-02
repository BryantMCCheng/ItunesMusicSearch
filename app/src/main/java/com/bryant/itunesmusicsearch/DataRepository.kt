package com.bryant.itunesmusicsearch

import com.bryant.itunesmusicsearch.apis.RetrofitService
import com.bryant.itunesmusicsearch.db.History
import com.bryant.itunesmusicsearch.db.HistoryRoomDataBase
import kotlinx.coroutines.flow.Flow

object DataRepository {

    private val dbManager by lazy {
        HistoryRoomDataBase.getInstance()
    }

    suspend fun getSearchInfo(input: String) = RetrofitService.searchApi.getSearchInfo(input)

    fun getHistoryInfo(): Flow<List<History>> {
        return dbManager.historyDao().getHistory()
    }

    suspend fun saveHistory(keyword: String) {
        dbManager.historyDao().insertHistory(History(keyword))
    }
}