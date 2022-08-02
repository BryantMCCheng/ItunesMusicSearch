package com.bryant.itunesmusicsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bryant.itunesmusicsearch.DataRepository
import com.bryant.itunesmusicsearch.data.ResultsItem
import com.bryant.itunesmusicsearch.db.History
import kotlinx.coroutines.launch

class MusicViewModel(private val repository: DataRepository) : ViewModel() {

    private var _searchResult = MutableLiveData<List<ResultsItem>>()
    val searchResult: LiveData<List<ResultsItem>>
        get() = _searchResult

    private var _historyList = repository.getHistoryInfo().asLiveData()
    val historyList: LiveData<List<History>>
        get() = _historyList

    fun getSearchResult(input: String) {
        viewModelScope.launch {
            val result = repository.getSearchInfo(input)
            repository.saveHistory(input)
            if (result.isNotEmpty()) {
                _searchResult.postValue(result)
            }
        }
    }
}