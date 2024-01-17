package com.bryant.itunesmusicsearch.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.bryant.itunesmusicsearch.databinding.FragmentPlayerBinding
import com.bryant.itunesmusicsearch.ui.LoadingDialogFragment
import com.bryant.itunesmusicsearch.utils.ApplicationContext
import com.bumptech.glide.Glide
import timber.log.Timber

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val playerViewModel: PlayerViewModel by viewModels()
    private var exoPlayer: ExoPlayer? = null
    private val loading by lazy {
        LoadingDialogFragment.newInstance()
    }

    private val playbackStateListener = object : androidx.media3.common.Player.Listener {

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
            exoPlayer = ExoPlayer.Builder(ApplicationContext).build().apply {
                playWhenReady = true
                setMediaItem(getMediaItem(url), true)
                prepare()
                addListener(playbackStateListener)
            }
        }
    }

    private fun getMediaItem(url: String): MediaItem {
        return MediaItem.fromUri(url)
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