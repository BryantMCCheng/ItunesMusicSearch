package com.bryant.itunesmusicsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryant.itunesmusicsearch.data.Player

class PlayerViewModel : ViewModel() {
    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> get() = _player

    private val _playbackTime = MutableLiveData<Long>()
    val playbackTime: LiveData<Long> get() = _playbackTime

    fun setPlayer(player: Player) {
        _player.value = player
    }

    fun setPlaybackTime(time: Long) {
        _playbackTime.value = time
    }
}