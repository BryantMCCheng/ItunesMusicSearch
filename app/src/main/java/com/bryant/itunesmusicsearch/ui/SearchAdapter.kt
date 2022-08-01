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
        private val binding: MusicItemBinding,
        private val clickListener: OnSearchItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(infoItem: ResultsItem) {
            infoItem.apply {
                binding.tvArtist.text = artistName
                binding.tvTrack.text = trackName
                val url: String = when {
                    artworkUrl100.isNotEmpty() -> artworkUrl100
                    artworkUrl60.isNotEmpty() -> artworkUrl60
                    artworkUrl30.isNotEmpty() -> artworkUrl30
                    else -> ""
                }
                if (url != "") {
                    Glide.with(ApplicationContext)
                        .load(url)
                        .into(binding.ivCover)
                }
                binding.root.setOnClickListener {
                    clickListener.onItemClick(
                        Player(
                            url,
                            trackName,
                            artistName,
                            previewUrl
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            MusicItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        Timber.d("position = $position")
        holder.bind(infoList[position])
    }

    override fun getItemCount() = infoList.size
}
