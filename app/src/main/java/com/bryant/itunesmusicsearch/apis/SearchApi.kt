package com.bryant.itunesmusicsearch.apis

import com.bryant.itunesmusicsearch.data.remote.dto.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET(ApiConfig.SEARCH)
    suspend fun getSearchInfo(@Query("term") term: String): Response<SearchResponse>
}