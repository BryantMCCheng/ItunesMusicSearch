package com.bryant.itunesmusicsearch.extensions

import android.content.Context
import android.view.View
import com.bryant.itunesmusicsearch.MainApplication
import com.bryant.itunesmusicsearch.utils.Utils

val ApplicationContext: Context
    get() = MainApplication.instance.applicationContext

fun isNetworkAvailable(offlineAction: () -> Unit): Boolean {
    val result = Utils.checkForInternet(ApplicationContext)
    if (!result) {
        offlineAction()
    }
    return result
}

fun setViewVisibility(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}