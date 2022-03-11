package me.kofesst.android.moneyapp.view.recyclerview

import me.kofesst.android.moneyapp.databinding.SubscriptionItemBinding
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.model.default.SubscriptionTypes
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatWithCurrency

class SubscriptionViewHolder(
    binding: SubscriptionItemBinding
) : BaseViewHolder<SubscriptionEntity>(binding.root) {
    private val nameText = binding.nameText
    private val amountText = binding.amountText
    private val actionText = binding.actionInfoText

    override fun bind(item: SubscriptionEntity) {
        nameText.text = item.title

        val amount = item.amount * if (item.type == SubscriptionTypes.DEBIT.ordinal) -1 else 1
        amountText.text = amount.formatWithCurrency(sign = true)
        amountText.setTextColor(amount.balanceColor(itemView.context))

        actionText.text = item.day.toString()
        // TODO replace day.toString() to next debit/credit date
    }
}