package me.kofesst.android.moneyapp.view.recyclerview

import me.kofesst.android.moneyapp.databinding.AssetItemBinding
import me.kofesst.android.moneyapp.model.AssetWithSubscriptions
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatWithCurrency

class AssetViewHolder(
    binding: AssetItemBinding
) : BaseViewHolder<AssetWithSubscriptions>(binding.root) {
    private val typeIcon = binding.typeIcon
    private val nameText = binding.nameText
    private val balanceText = binding.balanceText

    override fun bind(item: AssetWithSubscriptions) {
        val itemType = AssetTypes.values()[item.asset.type]
        typeIcon.setImageResource(itemType.iconRes)

        nameText.text = item.asset.name
        balanceText.text = item.asset.balance.formatWithCurrency()
        balanceText.setTextColor(item.asset.balance.balanceColor(itemView.context))
    }
}