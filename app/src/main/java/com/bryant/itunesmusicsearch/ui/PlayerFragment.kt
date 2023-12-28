package com.bryant.itunesmusicsearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bryant.itunesmusicsearch.data.Player
import com.bryant.itunesmusicsearch.databinding.FragmentPlayerBinding
import com.bryant.itunesmusicsearch.extensions.ApplicationContext
import com.bryant.itunesmusicsearch.viewmodel.PlayerViewModel
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Tracks
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import timber.log.Timber
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val playerViewModel: PlayerViewModel by viewModels()
    private var exoPlayer: ExoPlayer? = null
    private val loading by lazy {
        LoadingDialogFragment.newInstance()
    }

    private val playbackStateListener = object : com.google.android.exoplayer2.Player.Listener {
        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
        }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val player = PlayerFragmentArgs.fromBundle(it).player
            playerViewModel.setPlayer(player)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel.player.observe(viewLifecycleOwner) { player ->
            setupViews(player)
            initPlayer(player.previewUrl)
        }
        playerViewModel.playbackTime.observe(viewLifecycleOwner) { time ->
            // 將時間轉換為您想要的格式
            val formattedTime = formatPlaybackTime(time)
            binding.tvPlaybackTime.text = formattedTime
        }
    }

    private fun formatPlaybackTime(time: Long): String {
        // 實現您的時間格式轉換邏輯
        // 這裡只是一個簡單的例子，您可能需要根據您的需求進行更複雜的格式轉換
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun setupViews(player: Player) {
        loading.show(childFragmentManager, TAG)
        Glide.with(ApplicationContext).load(player.imageUrl).into(binding.ivCover)
        binding.tvTrackName.text = player.trackName
        binding.tvArtistName.text = player.artistName
    }

    private fun initPlayer(previewUrl: String?) {
        Timber.d("initPlayer")
        previewUrl?.let { url ->
            val mediaDataSourceFactory: DataSource.Factory =
                DefaultDataSource.Factory(ApplicationContext)

            val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))

            val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

            exoPlayer =
                ExoPlayer.Builder(ApplicationContext).setMediaSourceFactory(mediaSourceFactory)
                    .build()

            exoPlayer?.run {
                addMediaSource(mediaSource)
                prepare()
                playWhenReady = true
                addListener(playbackStateListener)
            }
        }
    }

    private fun releasePlayer() {
        Timber.d("exoplayer is released")
        exoPlayer?.removeListener(playbackStateListener)
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun onDestroyView() {
        Timber.d("onDestroyView")
        super.onDestroyView()
        releasePlayer()
        _binding = null
    }

    companion object {
        private val TAG = PlayerFragment::class.java.simpleName
    }
}