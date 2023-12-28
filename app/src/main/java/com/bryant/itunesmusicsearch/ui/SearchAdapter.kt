package com.bryant.itunesmusicsearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bryant.itunesmusicsearch.data.Player
import com.bryant.itunesmusicsearch.data.ResultsItem
import com.bryant.itunesmusicsearch.databinding.MusicItemBinding
import com.bryant.itunesmusicsearch.extensions.ApplicationContext
import com.bumptech.glide.Glide
import timber.log.Timber

interface OnSearchItemClickListener {
    fun onItemClick(player: Player)
}

class SearchAdapter(private val clickListener: OnSearchItemClickListener) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var infoList: List<ResultsItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class SearchViewHolder(
        private val binding: MusicItemBinding, private val clickListener: OnSearchItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(infoItem: ResultsItem) {
            binding.apply {
                tvArtist.text = infoItem.artistName
                tvTrack.text = infoItem.trackName
                val url: String = when {
                    infoItem.artworkUrl100.isNotEmpty() -> infoItem.artworkUrl100
                    infoItem.artworkUrl60.isNotEmpty() -> infoItem.artworkUrl60
                    infoItem.artworkUrl30.isNotEmpty() -> infoItem.artworkUrl30
                    else -> ""
                }
                if (url != "") {
                    Glide.with(ApplicationContext).load(url).into(ivCover)
                }
                root.setOnClickListener {
                    clickListener.onItemClick(
                        Player(
                            url, infoItem.trackName, infoItem.artistName, infoItem.previewUrl
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(inflater, clickListener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        Timber.d("position = $position")
        holder.bind(infoList[position])
    }

    override fun getItemCount() = infoList.size
}
