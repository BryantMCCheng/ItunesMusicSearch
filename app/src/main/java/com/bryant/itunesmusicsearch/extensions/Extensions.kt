package com.bryant.itunesmusicsearch.extensions

import android.content.Context
import com.bryant.itunesmusicsearch.MainApplication

val Any.ApplicationContext: Context
    get() = MainApplication.instance.applicationContext