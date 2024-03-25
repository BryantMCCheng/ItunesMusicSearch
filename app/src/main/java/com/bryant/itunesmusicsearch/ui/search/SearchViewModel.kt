package com.bryant.itunesmusicsearch.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryant.itunesmusicsearch.domain.model.ApiResult
import com.bryant.itunesmusicsearch.domain.usecase.GetSearchHistoryUseCase
import com.bryant.itunesmusicsearch.domain.usecase.GetSearchUseCase
import com.bryant.itunesmusicsearch.domain.usecase.SaveSearchTermUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchUseCase: GetSearchUseCase,
    getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val saveSearchTermUseCase: SaveSearchTermUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchContract.State())
    val state: StateFlow<SearchContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchContract.Effect>()
    val effect: SharedFlow<SearchContract.Effect> = _effect.asSharedFlow()

    init {
        getSearchHistoryUseCase()
            .onEach { history ->
                _state.update { it.copy(history = history.reversed()) } // Reverse the history list
            }
            .launchIn(viewModelScope)
    }

    fun setIntent(intent: SearchContract.Intent) {
        when (intent) {
            is SearchContract.Intent.Search -> search(intent.term)
            is SearchContract.Intent.OnSearchFocusChanged -> onSearchFocusChanged(intent.hasFocus)
            is SearchContract.Intent.OnTrackClicked -> onTrackClicked(intent.track)
            is SearchContract.Intent.OnHistoryItemClicked -> search(intent.term, fromHistory = true)
        }
    }

    private fun search(term: String, fromHistory: Boolean = false) {
        var showLoadingJob: Job? = null

        getSearchUseCase(term)
            .onStart {
                saveSearchTermUseCase(term)
            }
            .onEach { result ->
                // If a final result arrives, cancel the job that would have shown the loading indicator.
                if (result !is ApiResult.Loading) {
                    showLoadingJob?.cancel()
                }

                _state.update {
                    when (result) {
                        is ApiResult.Loading -> {
                            showLoadingJob = viewModelScope.launch {
                                delay(300L) // Only show loading if it takes more than 300ms
                                _state.update { s -> s.copy(isLoading = true) }
                            }
                            it.copy(isHistoryVisible = false)
                        }
                        is ApiResult.Success -> {
                            if (result.data.isNotEmpty()) {
                                viewModelScope.launch { _effect.emit(SearchContract.Effect.ScrollToTop) }
                            }
                            it.copy(
                                isLoading = false,
                                tracks = result.data,
                                notFoundKeyword = if (result.data.isEmpty()) term else null
                            )
                        }
                        is ApiResult.Error -> {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onSearchFocusChanged(hasFocus: Boolean) {
        _state.update { it.copy(isHistoryVisible = hasFocus) }
    }

    private fun onTrackClicked(track: com.bryant.itunesmusicsearch.domain.model.Track) {
        viewModelScope.launch {
            _effect.emit(SearchContract.Effect.NavigateToPlayer(track))
        }
    }
}