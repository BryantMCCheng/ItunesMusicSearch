package com.bryant.itunesmusicsearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bryant.itunesmusicsearch.DataRepository
import com.bryant.itunesmusicsearch.data.Player
import com.bryant.itunesmusicsearch.databinding.FragmentSearchBinding
import com.bryant.itunesmusicsearch.viewmodel.MusicViewModel
import timber.log.Timber

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val musicViewModel = MusicViewModel(DataRepository)

    private val searchAdapter by lazy {
        SearchAdapter(object : OnSearchItemClickListener {
            override fun onItemClick(player: Player) {
                findNavController().navigate(SearchFragmentDirections.goPlayer(player))
            }
        })
    }

    private val historyAdapter by lazy {
        HistoryAdapter(object : OnHistoryItemClickListener {
            override fun onItemClick(keyword: String) {
//                    binding.searchView.
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()
//        binding.buttonFirst.setOnClickListener {
//            val player = Player(
//                "https://is3-ssl.mzstatic.com/image/thumb/Music125/v4/79/1e/dc/791edcf0-73ab-96e7-b6c4-29943a79c14c/14UMGIM27067.rgb.jpg/100x100bb.jpg",
//                "Moves Like Jaggerasdauibvqwrevuwqriovrwmqiovmqwivomqwjvrqwmvqwrjiovjqwvioqwrrv (feat. Christina Aguilera)",
//                "Maroon 5",
//                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/12/d3/24/12d3248d-8b3b-53fd-1249-3bfb1834d0a0/mzaf_17675713238865539816.plus.aac.p.m4a"
//            )
//            findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToPlayerFragment(player))
//        }
    }

    private fun setViews() {
        binding.rvResult.apply {
            adapter = searchAdapter
        }

        binding.rvHistory.apply {
            adapter = historyAdapter
        }

        musicViewModel.searchResult.observe(viewLifecycleOwner) {
            Timber.d("search data update")
            searchAdapter.infoList = it
            binding.rvResult.visibility = View.VISIBLE
        }

        musicViewModel.historyList.observe(viewLifecycleOwner) {
            Timber.d("history data update")
            historyAdapter.historyList = it
        }

    }

    override fun onStart() {
        super.onStart()
        musicViewModel.getSearchResult()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}