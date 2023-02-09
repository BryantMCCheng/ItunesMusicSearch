package com.bryant.itunesmusicsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bryant.itunesmusicsearch.DataRepository
import com.bryant.itunesmusicsearch.ListState
import com.bryant.itunesmusicsearch.NetworkResult
import com.bryant.itunesmusicsearch.data.MusicItem
import com.bryant.itunesmusicsearch.db.History
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.SocketTimeoutException

class MusicViewModel(private val repository: DataRepository) : ViewModel() {

    private var _searchResponse =
        MutableStateFlow<NetworkResult<List<MusicItem>>>(NetworkResult.Loading(false))
    val searchResponse: StateFlow<NetworkResult<List<MusicItem>>> = _searchResponse

    private var _searchResult = MutableLiveData<List<MusicItem>>()
    val searchResult: LiveData<List<MusicItem>>
        get() = _searchResult

    private var _historyList = repository.getHistory().asLiveData()
    val historyList: LiveData<List<History>>
        get() = _historyList

    private var _listState = MutableLiveData<ListState>()
    val listState: LiveData<ListState>
        get() = _listState

    fun updateListState(state: ListState) {
        _listState.value = state
    }

    fun getSearchResult(input: String) {
        viewModelScope.launch(exceptionHandler) {
//            updateListState(ListState.Searching)
            repository.saveHistory(input)
            repository.getSearchInfo(input).collect {
                Timber.d("collect, current thread = ${Thread.currentThread()}")
                _searchResponse.value = it
            }
//            val response = repository.getSearchInfo(input)
//            if (response.isSuccessful) {
//                _searchResult.value = response.body()?.results
//                _searchResult.value?.let {
//                    if (it.isEmpty()) {
//                        updateListState(ListState.NotFound(input))
//                    }
//                }
//                updateListState(ListState.ShowResult)
//            } else {
//                onError("Error : ${response.message()} ")
//            }
        }
    }

    private fun onError(message: String) {
        updateListState(ListState.Error(message))
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared")
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