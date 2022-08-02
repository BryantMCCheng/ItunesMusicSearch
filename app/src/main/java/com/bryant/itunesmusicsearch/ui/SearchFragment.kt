package com.bryant.itunesmusicsearch.ui

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bryant.itunesmusicsearch.DataRepository
import com.bryant.itunesmusicsearch.R
import com.bryant.itunesmusicsearch.data.Player
import com.bryant.itunesmusicsearch.databinding.FragmentSearchBinding
import com.bryant.itunesmusicsearch.extensions.ApplicationContext
import com.bryant.itunesmusicsearch.extensions.isNetworkAvailable
import com.bryant.itunesmusicsearch.extensions.setViewVisibility
import com.bryant.itunesmusicsearch.viewmodel.MusicViewModel
import timber.log.Timber

class SearchFragment : Fragment(), MenuProvider {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchView: SearchView

    private val musicViewModel = MusicViewModel(DataRepository)
    private val loading by lazy {
        LoadingDialogFragment.newInstance()
    }

    private val searchAdapter by lazy {
        SearchAdapter(object : OnSearchItemClickListener {
            override fun onItemClick(player: Player) {
                if (isNetworkAvailable()) {
                    findNavController().navigate(SearchFragmentDirections.goPlayer(player))
                }
            }
        })
    }

    private val historyAdapter by lazy {
        HistoryAdapter(object : OnHistoryItemClickListener {
            override fun onItemClick(keyword: String) {
                if (isNetworkAvailable()) {
                    searchView.setQuery(keyword, true)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
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
            setViewVisibility(binding.rvResult, true)
            if (it.isEmpty()) {
                Toast.makeText(ApplicationContext, "No music found", Toast.LENGTH_SHORT).show()
            }
        }

        musicViewModel.historyList.observe(viewLifecycleOwner) {
            Timber.d("history data update")
            it?.let {
                historyAdapter.historyList = it.reversed()
            }
        }

        musicViewModel.errorMessage.observe(viewLifecycleOwner) {
            Timber.d("errorMessage = $it")
            Toast.makeText(ApplicationContext, it, Toast.LENGTH_SHORT).show()
        }

        musicViewModel.loading.observe(viewLifecycleOwner, Observer {
            Timber.d("loading = $it")
            if (it) {
                loading.show(childFragmentManager, TAG)
            } else {
                loading.dismiss()
            }
        })

    }

    override fun onDestroyView() {
        Timber.d("onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextFocusChangeListener { _, hasFocus ->
                setViewVisibility(binding.rvHistory, hasFocus)
                setViewVisibility(binding.rvResult, !hasFocus)
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String?): Boolean {
                    if (isNetworkAvailable()) {
                        s?.let {
                            binding.rvResult.scrollToPosition(0)
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
        private const val TAG = "SearchFragment"
    }
}