package com.bryant.itunesmusicsearch

import com.bryant.itunesmusicsearch.apis.RetrofitService
import com.bryant.itunesmusicsearch.data.ResultsItem

object DataRepository {
    suspend fun getSearchInfo(input: String): List<ResultsItem>? {
        val result = RetrofitService.searchApi.getSearchInfo(input)
        return if (result.isSuccessful) {
            result.body()?.results
        } else {
            emptyList()
        }
    }

    suspend fun getHistoryInfo() = null
}