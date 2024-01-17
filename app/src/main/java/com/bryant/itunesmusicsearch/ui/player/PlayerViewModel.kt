package com.bryant.itunesmusicsearch.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {
    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> get() = _player

    fun setPlayer(player: Player) {
        _player.value = player
    }
}