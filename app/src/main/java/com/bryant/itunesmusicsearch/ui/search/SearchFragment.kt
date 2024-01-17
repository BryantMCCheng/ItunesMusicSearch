package com.bryant.itunesmusicsearch.ui.search

import HistoryScreen
import SearchResultScreen
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bryant.itunesmusicsearch.ListState
import com.bryant.itunesmusicsearch.R
import com.bryant.itunesmusicsearch.data.DataRepository
import com.bryant.itunesmusicsearch.databinding.FragmentSearchBinding
import com.bryant.itunesmusicsearch.ui.history.HistoryAdapter
import com.bryant.itunesmusicsearch.ui.LoadingDialogFragment
import com.bryant.itunesmusicsearch.ui.history.OnHistoryItemClickListener
import com.bryant.itunesmusicsearch.ui.player.Player
import com.bryant.itunesmusicsearch.ui.theme.MyApplicationTheme
import com.bryant.itunesmusicsearch.utils.isNetworkAvailable
import com.bryant.itunesmusicsearch.utils.showToast
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class SearchFragment : Fragment(), MenuProvider {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchView: SearchView

    private val musicViewModel by lazy {
        ViewModelProvider(
            this, MusicViewModel.Factory(DataRepository(Dispatchers.IO))
        )[MusicViewModel::class.java]
    }
    private val loading by lazy {
        LoadingDialogFragment.newInstance()
    }

    private val searchAdapter by lazy {
        SearchAdapter(object : OnSearchItemClickListener {
            override fun onItemClick(player: Player) {
                if (isNetworkAvailable(offlineAction)) {
                    findNavController().navigate(SearchFragmentDirections.goPlayer(player))
                }
            }
        })
    }

    private val historyAdapter by lazy {
        HistoryAdapter(object : OnHistoryItemClickListener {
            override fun onItemClick(keyword: String) {
                if (isNetworkAvailable(offlineAction)) {
                    searchView.setQuery(keyword, true)
                }
            }
        })
    }

    private val offlineAction: () -> Unit = {
        musicViewModel.updateListState(ListState.Offline)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupComposeScreen()
        setupMenuProvider()
        return binding.root
    }

    private fun setupComposeScreen() {
        binding.composeContainer.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MyApplicationTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        val listState by musicViewModel.listState.observeAsState()
                        when (listState) {
                            ListState.ShowHistory -> {
                                HistoryScreen(musicViewModel = musicViewModel)
                            }

                            ListState.ShowSearchResult -> {
                                SearchResultScreen(
                                    musicViewModel = musicViewModel,
                                    navController = findNavController()
                                )
                            }

                            else -> {
                                // do nothing
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupMenuProvider() {
        activity?.let { activity ->
            val menuHost: MenuHost = activity
            menuHost.addMenuProvider(
                this@SearchFragment, viewLifecycleOwner, Lifecycle.State.RESUMED
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.rvResult.apply {
            adapter = searchAdapter
        }

        binding.rvHistory.apply {
            adapter = historyAdapter
        }
    }

    private fun setupObservers() {
//        searchViewModel.searchResult.observe(viewLifecycleOwner) {
//            Timber.d("search data update")
//            searchAdapter.infoList = it
//        }
//
//        searchViewModel.historyList.observe(viewLifecycleOwner) {
//            Timber.d("history data update")
//            it?.let {
//                historyAdapter.historyList = it.reversed()
//            }
//        }

        musicViewModel.listState.observe(viewLifecycleOwner) {
            handleViewState(it)
        }
    }

    private fun handleViewState(state: ListState) {
        Timber.d("state = $state")

        when (state) {
            is ListState.Searching -> {
                loading.show(childFragmentManager, TAG)
            }

            is ListState.ShowSearchResult -> {
//                updateVisibility(binding.rvResult, true)
//                updateVisibility(binding.rvHistory, false)
                restoreSearchView()
                loading.dismiss()
            }

            is ListState.ShowHistory -> {
//                updateVisibility(binding.rvResult, false)
//                updateVisibility(binding.rvHistory, true)
            }

            is ListState.Error, is ListState.NotFound, is ListState.Offline, is ListState.Timeout -> {
                showToast(
                    context, when (state) {
                        is ListState.Error -> state.msg
                        is ListState.NotFound -> "No music found, keyword is: ${state.keyword}"
                        is ListState.Offline -> "The network is offline, please check your network status..."
                        is ListState.Timeout -> "socket time out"
                        else -> ""
                    }
                )
                restoreSearchView()
                loading.dismiss()
            }
        }
    }

    private fun restoreSearchView() {
        if (!searchView.isIconified) {
            searchView.isIconified = true;
            searchView.onActionViewCollapsed()
        }
    }

    override fun onDestroyView() {
        Timber.d("onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.apply {
            setOnQueryTextFocusChangeListener { _, hasFocus ->
                musicViewModel.updateListState(
                    if (hasFocus) {
                        ListState.ShowHistory
                    } else {
                        ListState.ShowSearchResult
                    }
                )
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String?): Boolean {
                    if (isNetworkAvailable(offlineAction)) {
                        s?.let {
//                            binding.rvResult.scrollToPosition(0)
                            musicViewModel.getSearchResult(it)
                            searchView.clearFocus()
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(s: String?): Boolean {
                    return true
                }

            })
        }
    }

    override fun onMenuItemSelected(p0: MenuItem): Boolean {
        return true
    }

    companion object {
        private val TAG = SearchFragment::class.java.simpleName
    }
}