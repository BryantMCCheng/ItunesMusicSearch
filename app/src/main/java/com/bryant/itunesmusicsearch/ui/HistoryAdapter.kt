package com.bryant.itunesmusicsearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bryant.itunesmusicsearch.data.HistoryInfo
import com.bryant.itunesmusicsearch.data.HistoryInfoItem
import com.bryant.itunesmusicsearch.databinding.HistoryItemBinding
import timber.log.Timber

interface OnHistoryItemClickListener {
    fun onItemClick(keyword: String)
}

class HistoryAdapter(private val clickListener: OnHistoryItemClickListener) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    var historyList = HistoryInfo()

    class HistoryViewHolder(
        private val binding: HistoryItemBinding,
        private val clickListener: OnHistoryItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(infoItem: HistoryInfoItem) {
            infoItem.apply {
                binding.tvKeyword.text = keyword
                binding.root.setOnClickListener {
                    clickListener.onItemClick(
                        keyword
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        Timber.d("position = $position")
        holder.bind(historyList[position])
    }

    override fun getItemCount() = historyList.size
}