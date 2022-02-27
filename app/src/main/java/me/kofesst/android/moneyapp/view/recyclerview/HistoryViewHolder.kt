package me.kofesst.android.moneyapp.view.recyclerview

import me.kofesst.android.moneyapp.databinding.HistoryItemBinding
import me.kofesst.android.moneyapp.model.relation.TransactionDetails
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.format

class HistoryViewHolder(
    binding: HistoryItemBinding
): BaseViewHolder<TransactionDetails>(binding.root) {
    private val titleText = binding.titleText
    private val amountText = binding.amountText
    private val categoryText = binding.categoryText

    override fun bind(item: TransactionDetails) {
        titleText.text = item.transaction.title

        amountText.text = item.transaction.amount.format()
        amountText.setTextColor(item.transaction.amount.balanceColor(itemView.context))

        categoryText.text = item.category.name
    }
}