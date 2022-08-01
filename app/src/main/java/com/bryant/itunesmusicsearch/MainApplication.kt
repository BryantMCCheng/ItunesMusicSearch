package com.bryant.itunesmusicsearch

import android.app.Application
import com.bryant.itunesmusicsearch.log.MultiTagTree
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(MultiTagTree())
        }
    }

    companion object {
        internal lateinit var instance: Application
    }
}