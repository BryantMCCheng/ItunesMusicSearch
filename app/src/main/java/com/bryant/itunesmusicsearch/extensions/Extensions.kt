package com.bryant.itunesmusicsearch.extensions

import android.content.Context
import com.bryant.itunesmusicsearch.MainApplication
import com.bryant.itunesmusicsearch.Utils

val Any.ApplicationContext: Context
    get() = MainApplication.instance.applicationContext

fun Any.isNetworkAvailable(context: Context) = Utils.checkForInternet(context)