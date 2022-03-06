package me.kofesst.android.moneyapp.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import me.kofesst.android.moneyapp.databinding.HistoryItemBinding
import me.kofesst.android.moneyapp.model.TransactionEntity

class HistoryAdapter(
    private val context: Context
): PagingDataAdapter<TransactionEntity, HistoryViewHolder>(ItemsDiffBuilder()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(context)
        val viewHolderBinding = HistoryItemBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(viewHolderBinding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position) ?: return
        holder.bind(history)
    }

}