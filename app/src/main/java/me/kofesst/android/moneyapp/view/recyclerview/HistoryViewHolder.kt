package me.kofesst.android.moneyapp.view.recyclerview

import android.view.View
import me.kofesst.android.moneyapp.databinding.HistoryItemBinding
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatDate
import me.kofesst.android.moneyapp.util.formatWithCurrency

class HistoryViewHolder(
    binding: HistoryItemBinding
): BaseViewHolder<TransactionEntity>(binding.root) {
    private val titleText = binding.titleText
    private val amountText = binding.amountText
    private val assetNameText = binding.assetNameText
    private val categoryText = binding.categoryText
    private val dateText = binding.dateText
    private val targetRow = binding.targetRow
    private val targetNameText = binding.targetNameText
    private val targetAmountText = binding.targetAmountText

    override fun bind(item: TransactionEntity) {
        titleText.text = item.title

        amountText.text = item.amount.formatWithCurrency(sign = true)
        amountText.setTextColor(item.amount.balanceColor(itemView.context))

        assetNameText.text = item.assetName
        categoryText.text = item.categoryName
        dateText.text = item.date.formatDate()

        if (item.targetName == null) {
            targetRow.visibility = View.GONE
            return
        }

        targetNameText.text = item.targetName
        targetAmountText.text = (-item.amount).formatWithCurrency(sign = true)
        targetAmountText.setTextColor((-item.amount).balanceColor(itemView.context))
    }
}