package me.kofesst.android.moneyapp.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import me.kofesst.android.moneyapp.databinding.CategoryItemBinding
import me.kofesst.android.moneyapp.model.CategoryEntity

class CategoriesAdapter(
    private val context: Context
): BaseAdapter<CategoryEntity, CategoryViewHolder>(DIFFER) {
    companion object {
        private val DIFFER = ItemsDiffBuilder<CategoryEntity>().apply {
            itemsCheck = { old, new -> old.id == new.id }
            contentsCheck = { old, new -> old.name == new.name }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(context)
        val viewHolderBinding = CategoryItemBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(viewHolderBinding)
    }
}