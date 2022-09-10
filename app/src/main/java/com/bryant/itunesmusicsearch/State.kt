package com.bryant.itunesmusicsearch

sealed class ListState {
    object ShowResult : ListState()
    object ShowHistory : ListState()
    object Searching : ListState()
    object Timeout : ListState()
    object Offline : ListState()
    class NotFound(var keyword: String?) : ListState()
    class Error(var msg: String) : ListState()
}

sealed class NetworkResult<T> {
    data class Loading<T>(val isLoading: Boolean) : NetworkResult<T>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Failure<T>(val errorMessage: String) : NetworkResult<T>()
}