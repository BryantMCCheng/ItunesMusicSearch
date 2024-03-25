package com.bryant.itunesmusicsearch.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bryant.itunesmusicsearch.databinding.HistoryItemBinding

interface OnHistoryItemClickListener {
    fun onItemClick(keyword: String)
}

class HistoryAdapter(private val clickListener: OnHistoryItemClickListener) :
    ListAdapter<String, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(
        private val binding: HistoryItemBinding,
        private val clickListener: OnHistoryItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(keyword: String) {
            binding.tvKeyword.text = keyword
            binding.root.setOnClickListener { clickListener.onItemClick(keyword) }
        }
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}