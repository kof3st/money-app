package me.kofesst.android.moneyapp.view.recyclerview

import me.kofesst.android.moneyapp.databinding.CategoryItemBinding
import me.kofesst.android.moneyapp.model.CategoryEntity

class CategoryViewHolder(
    binding: CategoryItemBinding
) : BaseViewHolder<CategoryEntity>(binding.root) {
    private val nameText = binding.nameText

    override fun bind(item: CategoryEntity) {
        nameText.text = item.name
    }
}