package me.kofesst.android.moneyapp.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import me.kofesst.android.moneyapp.databinding.HistoryItemBinding
import me.kofesst.android.moneyapp.model.relation.TransactionDetails

class HistoryAdapter(
    private val context: Context
): BaseAdapter<TransactionDetails, HistoryViewHolder>(ItemsDiffBuilder()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(context)
        val viewHolderBinding = HistoryItemBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(viewHolderBinding)
    }

}