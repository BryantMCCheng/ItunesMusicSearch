package com.bryant.itunesmusicsearch.ui.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bryant.itunesmusicsearch.domain.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _track = MutableStateFlow(savedStateHandle.get<Track>("track"))
    val track: StateFlow<Track?> = _track.asStateFlow()

}