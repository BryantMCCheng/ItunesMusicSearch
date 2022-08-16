package com.bryant.itunesmusicsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bryant.itunesmusicsearch.DataRepository
import com.bryant.itunesmusicsearch.ListState
import com.bryant.itunesmusicsearch.data.ResultsItem
import com.bryant.itunesmusicsearch.db.History
import com.bryant.itunesmusicsearch.extensions.safeLaunch
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.SocketTimeoutException

class MusicViewModel(private val repository: DataRepository) : ViewModel() {

    private var _searchResult = MutableLiveData<List<ResultsItem>>()
    val searchResult: LiveData<List<ResultsItem>>
        get() = _searchResult

    private var _historyList = repository.getHistoryInfo().asLiveData()
    val historyList: LiveData<List<History>>
        get() = _historyList

    private var _listState = MutableLiveData<ListState>()
    val listState: LiveData<ListState>
        get() = _listState

    var job: Job? = null

    fun updateListState(state: ListState) {
        _listState.value = state
    }

    fun getSearchResult(input: String) {
        job = viewModelScope.safeLaunch(exceptionHandler) {
            withContext(Dispatchers.Main) {
                updateListState(ListState.Searching)
            }
            repository.saveHistory(input)
            val response = repository.getSearchInfo(input)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _searchResult.value = response.body()?.results
                    _searchResult.value?.let {
                        if (it.isEmpty()) {
                            updateListState(ListState.NotFound(input))
                        }
                    }
                    updateListState(ListState.ShowResult)
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        updateListState(ListState.Error(message))
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared")
        job?.cancel()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is SocketTimeoutException -> updateListState(ListState.Timeout)
            else -> onError("Exception handled: ${throwable.localizedMessage}")
        }
    }

    /**
     * Factory for constructing MusicViewModel with parameter
     */
    class Factory(private val repository: DataRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MusicViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MusicViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}