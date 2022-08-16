package com.bryant.itunesmusicsearch.apis

import com.bryant.itunesmusicsearch.data.SearchResult
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object RetrofitService {

    private val okhttpClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(ApiConfig.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(ApiConfig.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
        .build()

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient)
            .build()
    }

    val searchApi: SearchApi by lazy {
        retrofit().create(SearchApi::class.java)
    }
}

interface SearchApi {
    @GET(ApiConfig.SEARCH)
    suspend fun getSearchInfo(
        @Query(value = "term", encoded = true) input: String
    ): Response<SearchResult>
}