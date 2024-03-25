package com.bryant.itunesmusicsearch.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bryant.itunesmusicsearch.R
import com.bryant.itunesmusicsearch.databinding.FragmentSearchBinding
import com.bryant.itunesmusicsearch.domain.model.Track
import com.bryant.itunesmusicsearch.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), MenuProvider, OnSearchItemClickListener, OnHistoryItemClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val searchAdapter by lazy { SearchAdapter(this) }
    private val historyAdapter by lazy { HistoryAdapter(this) }

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupUI() {
        binding.rvResult.adapter = searchAdapter
        binding.rvHistory.adapter = historyAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        renderState(state)
                    }
                }
                launch {
                    viewModel.effect.collect { effect ->
                        renderEffect(effect)
                    }
                }
            }
        }
    }

    private fun renderState(state: SearchContract.State) {
        // Show the progress bar as an overlay
        binding.progressBar.isVisible = state.isLoading

        searchAdapter.submitList(state.tracks)
        historyAdapter.submitList(state.history)

        // The result list should only be hidden when the history is visible
        binding.rvResult.isVisible = !state.isHistoryVisible
        binding.rvHistory.isVisible = state.isHistoryVisible

        state.error?.let { 
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        state.notFoundKeyword?.let {
            Toast.makeText(context, "No music found for: $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderEffect(effect: SearchContract.Effect) {
        when (effect) {
            is SearchContract.Effect.NavigateToPlayer -> {
                findNavController().navigate(SearchFragmentDirections.goPlayer(effect.track))
            }
            is SearchContract.Effect.ScrollToTop -> {
                binding.rvResult.scrollToPosition(0)
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
        searchMenuItem = menu.findItem(R.id.action_search)
        searchView = (searchMenuItem?.actionView as? SearchView)?.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrBlank()) {
                        viewModel.setIntent(SearchContract.Intent.Search(query))
                        view?.hideKeyboard()
                        searchMenuItem?.collapseActionView()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?) = true
            })
        }

        searchMenuItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                viewModel.setIntent(SearchContract.Intent.OnSearchFocusChanged(true))
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.setIntent(SearchContract.Intent.OnSearchFocusChanged(false))
                return true
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false

    override fun onItemClick(track: Track) {
        viewModel.setIntent(SearchContract.Intent.OnTrackClicked(track))
    }

    override fun onItemClick(keyword: String) {
        searchView?.setQuery(keyword, true)
    }

    override fun onDestroyView() {
        binding.rvResult.adapter = null
        binding.rvHistory.adapter = null
        _binding = null
        super.onDestroyView()
    }
}