package com.bryant.itunesmusicsearch.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.bryant.itunesmusicsearch.databinding.FragmentPlayerBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModels()

    private var exoPlayer: ExoPlayer? = null

    private val playbackStateListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY"
                ExoPlayer.STATE_ENDED -> {
                    findNavController().navigateUp()
                    Toast.makeText(requireContext(), "Music playback ended.", Toast.LENGTH_SHORT).show()
                    "ExoPlayer.STATE_ENDED"
                }
                else -> "UNKNOWN_STATE"
            }
            Timber.d("changed state to $stateString")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.track.collectLatest { track ->
                track?.let {
                    setupViews(it)
                    initPlayer(it.previewUrl)
                }
            }
        }
    }

    private fun setupViews(track: com.bryant.itunesmusicsearch.domain.model.Track) {
        Glide.with(this).load(track.artworkUrl).into(binding.ivCover)
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
    }

    private fun initPlayer(previewUrl: String?) {
        if (previewUrl.isNullOrEmpty()) {
            Toast.makeText(context, "No preview available", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        exoPlayer = ExoPlayer.Builder(requireContext()).build().apply {
            playWhenReady = true
            setMediaItem(MediaItem.fromUri(previewUrl))
            prepare()
            addListener(playbackStateListener)
        }
        binding.playerView.player = exoPlayer
    }

    private fun releasePlayer() {
        exoPlayer?.let {
            it.removeListener(playbackStateListener)
            it.release()
        }
        exoPlayer = null
    }

    override fun onDestroyView() {
        releasePlayer()
        _binding = null
        super.onDestroyView()
    }
}