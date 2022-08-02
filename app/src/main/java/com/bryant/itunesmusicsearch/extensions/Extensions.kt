package com.bryant.itunesmusicsearch.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import com.bryant.itunesmusicsearch.MainApplication
import com.bryant.itunesmusicsearch.utils.Utils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val ApplicationContext: Context
    get() = MainApplication.instance.applicationContext

fun isNetworkAvailable(): Boolean {
    val result = Utils.checkForInternet(ApplicationContext)
    if (!result) {
        Toast.makeText(
            ApplicationContext,
            "The network is offline, please check your network status...",
            Toast.LENGTH_SHORT
        ).show()
    }
    return result
}

fun setViewVisibility(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

fun CoroutineScope.safeLaunch(
    exceptionHandler: CoroutineExceptionHandler = coroutineExceptionHandler,
    launchBody: suspend () -> Unit
): Job {
    return this.launch(exceptionHandler) {
        launchBody.invoke()
    }
}

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}