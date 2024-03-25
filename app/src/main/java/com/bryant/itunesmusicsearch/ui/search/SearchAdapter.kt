package com.bryant.itunesmusicsearch.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bryant.itunesmusicsearch.databinding.MusicItemBinding
import com.bryant.itunesmusicsearch.domain.model.Track
import com.bumptech.glide.Glide

interface OnSearchItemClickListener {
    fun onItemClick(track: Track)
}

class SearchAdapter(private val clickListener: OnSearchItemClickListener) :
    ListAdapter<Track, SearchAdapter.SearchViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchViewHolder(
        private val binding: MusicItemBinding,
        private val clickListener: OnSearchItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.apply {
                tvArtist.text = track.artistName
                tvTrack.text = track.trackName
                if (track.artworkUrl.isNotEmpty()) {
                    Glide.with(itemView.context).load(track.artworkUrl).into(ivCover)
                }
                root.setOnClickListener { clickListener.onItemClick(track) }
            }
        }
    }
}

class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}