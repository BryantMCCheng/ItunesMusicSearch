package com.bryant.itunesmusicsearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bryant.itunesmusicsearch.data.Player
import com.bryant.itunesmusicsearch.databinding.FragmentPlayerBinding
import com.bryant.itunesmusicsearch.extensions.ApplicationContext
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.Util
import timber.log.Timber

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private var player: Player? = null
    private var exoPlayer: ExoPlayer? = null
    private val loading by lazy {
        LoadingDialogFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            player = PlayerFragmentArgs.fromBundle(it).player
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        setViews()
        return binding.root
    }

    private fun setViews() {
        loading.show(childFragmentManager, TAG)
        player?.let { data ->
            Glide.with(ApplicationContext).load(data.imageUrl).into(binding.ivCover)
            binding.tvTrackName.text = data.trackName
            binding.tvArtistName.text = data.artistName
        }
    }

    private fun initPlayer() {
        Timber.d("initPlayer")
        player?.previewUrl?.let { url ->
            val mediaDataSourceFactory: DataSource.Factory =
                DefaultDataSource.Factory(ApplicationContext)

            val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))

            val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

            exoPlayer = ExoPlayer.Builder(ApplicationContext)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()

            exoPlayer?.run {
                addMediaSource(mediaSource)
                prepare()
                playWhenReady = true
                addListener(playbackStateListener())
            }
        }
    }

    private fun playbackStateListener() = object : com.google.android.exoplayer2.Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString: String = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING"
                ExoPlayer.STATE_READY -> {
                    loading.dismiss()
                    "ExoPlayer.STATE_READY"
                }
                ExoPlayer.STATE_ENDED -> {
                    findNavController().navigateUp()
                    Toast.makeText(ApplicationContext, "Music playback ended.", Toast.LENGTH_SHORT)
                        .show()
                    "ExoPlayer.STATE_ENDED"
                }
                else -> "UNKNOWN_STATE"
            }
            Timber.d("changed state to $stateString")
        }
    }

    private fun releasePlayer() {
        Timber.d("exoplayer is released")
        exoPlayer?.release()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) initPlayer()
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) initPlayer()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) releasePlayer()
    }

    override fun onDestroyView() {
        Timber.d("onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PlayerFragment"
    }
}