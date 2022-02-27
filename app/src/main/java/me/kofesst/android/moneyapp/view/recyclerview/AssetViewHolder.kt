package me.kofesst.android.moneyapp.view.recyclerview

import me.kofesst.android.moneyapp.databinding.AssetItemBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatWithCurrency

class AssetViewHolder(
    binding: AssetItemBinding
): BaseViewHolder<AssetEntity>(binding.root) {
    private val typeIcon = binding.typeIcon
    private val nameText = binding.nameText
    private val balanceText = binding.balanceText

    override fun bind(item: AssetEntity) {
        val itemType = AssetTypes.values()[item.type]
        typeIcon.setImageResource(itemType.iconRes)

        nameText.text = item.name
        balanceText.text = item.balance.formatWithCurrency()
        balanceText.setTextColor(item.balance.balanceColor(itemView.context))
    }
}