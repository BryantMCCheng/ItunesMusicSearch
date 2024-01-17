package com.bryant.itunesmusicsearch

sealed class ListState {
    object ShowSearchResult : ListState()
    object ShowHistory : ListState()
    object Searching : ListState()
    object Timeout : ListState()
    object Offline : ListState()
    class NotFound(var keyword: String) : ListState()
    class Error(var msg: String) : ListState()
}
