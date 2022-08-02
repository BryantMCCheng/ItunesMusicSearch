package com.bryant.itunesmusicsearch

import com.bryant.itunesmusicsearch.apis.RetrofitService
import com.bryant.itunesmusicsearch.data.ResultsItem
import com.bryant.itunesmusicsearch.db.History
import com.bryant.itunesmusicsearch.db.HistoryRoomDataBase
import kotlinx.coroutines.flow.Flow

object DataRepository {

    private val searchApi by lazy {
        RetrofitService.searchApi
    }

    private val dbManager by lazy {
        HistoryRoomDataBase.getInstance()
    }

    suspend fun getSearchInfo(input: String): List<ResultsItem> {
        val result = searchApi.getSearchInfo(input)
        return if (result.isSuccessful) {
            result.body()?.results ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun getHistoryInfo(): Flow<List<History>> {
        return dbManager.historyDao().getHistory()
    }

    suspend fun saveHistory(keyword: String) {
        dbManager.historyDao().insertHistory(History(keyword))
    }
}