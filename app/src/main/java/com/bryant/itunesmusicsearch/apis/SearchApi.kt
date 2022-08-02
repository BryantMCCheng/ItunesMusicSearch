package com.bryant.itunesmusicsearch.apis

import com.bryant.itunesmusicsearch.apis.ApiConfig.SEARCH
import com.bryant.itunesmusicsearch.data.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET(SEARCH)
    suspend fun getSearchInfo(
        @Query(
            value = "term",
            encoded = true
        ) input: String
    ): Response<SearchResult>
}