package com.bryant.itunesmusicsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryant.itunesmusicsearch.DataRepository
import com.bryant.itunesmusicsearch.data.HistoryInfo
import com.bryant.itunesmusicsearch.data.ResultsItem
import kotlinx.coroutines.launch

class MusicViewModel(private val repository: DataRepository) : ViewModel() {

    private var _searchResult = MutableLiveData<List<ResultsItem>>()
    val searchResult: LiveData<List<ResultsItem>>
        get() = _searchResult
    private var _historyList = MutableLiveData<HistoryInfo>()
    val historyList: LiveData<HistoryInfo>
        get() = _historyList

    fun getSearchResult() {
        viewModelScope.launch {
            _searchResult.value = repository.getSearchInfo("jason+mraz")
        }
    }

    fun getHistory() {
        viewModelScope.launch {
            _historyList.value = repository.getHistoryInfo()
        }
    }
}