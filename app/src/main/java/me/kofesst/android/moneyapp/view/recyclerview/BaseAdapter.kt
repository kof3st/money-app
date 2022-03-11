package me.kofesst.android.moneyapp.view.recyclerview

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<M, VH : BaseViewHolder<M>>(
    itemsDiffCallback: DiffUtil.ItemCallback<M>
) : ListAdapter<M, VH>(itemsDiffCallback) {
    var itemClickListener: ItemClickListener<M>? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        if (itemClickListener == null) return

        holder.itemView.setOnClickListener {
            itemClickListener!!.onClick(it, item)
        }

        holder.itemView.setOnLongClickListener {
            itemClickListener!!.onLongClick(it, item)
            true
        }
    }

    override fun submitList(list: List<M>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}

interface ItemClickListener<T> {

    fun onClick(view: View, item: T)

    fun onLongClick(view: View, item: T)

}

class ItemsDiffBuilder<T> : DiffUtil.ItemCallback<T>() {
    var itemsCheck: (T, T) -> Boolean = { _, _ -> true }
    var contentsCheck: (T, T) -> Boolean = { _, _ -> true }

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        itemsCheck(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        contentsCheck(oldItem, newItem)

}