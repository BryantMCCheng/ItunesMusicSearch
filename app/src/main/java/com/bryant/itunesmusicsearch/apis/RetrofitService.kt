package com.bryant.itunesmusicsearch.apis

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    private val okhttpClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(ApiConfig.OKHTTP_TIMEOUT, TimeUnit.SECONDS)
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