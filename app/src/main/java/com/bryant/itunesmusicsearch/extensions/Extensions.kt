package com.bryant.itunesmusicsearch.extensions

import android.content.Context
import android.view.View
import com.bryant.itunesmusicsearch.MainApplication
import com.bryant.itunesmusicsearch.utils.Utils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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