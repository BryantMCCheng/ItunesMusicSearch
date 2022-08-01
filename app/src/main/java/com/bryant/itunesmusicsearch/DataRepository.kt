package com.bryant.itunesmusicsearch

import com.bryant.itunesmusicsearch.apis.RetrofitService
import com.bryant.itunesmusicsearch.data.ResultsItem
import com.bryant.itunesmusicsearch.db.History
import com.bryant.itunesmusicsearch.db.HistoryRoomDataBase
import kotlinx.coroutines.flow.Flow

object DataRepository {
    suspend fun getSearchInfo(input: String): List<ResultsItem> {
        val result = RetrofitService.searchApi.getSearchInfo(input)
        return if (result.isSuccessful) {
            result.body()?.results ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun getHistoryInfo(): Flow<List<History>> {
        return HistoryRoomDataBase.getInstance().historyDao().getHistory()
    }
}