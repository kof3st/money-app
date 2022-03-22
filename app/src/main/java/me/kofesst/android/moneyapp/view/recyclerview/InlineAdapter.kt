package me.kofesst.android.moneyapp.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

class InlineAdapter<Binding : ViewBinding, Model>(
    private val context: Context,
    private val bindingProducer: (LayoutInflater, ViewGroup) -> Binding,
    itemsComparator: (Model, Model) -> Boolean = { _, _ -> true },
    private val onItemClickListener: ItemClickListener<Binding, Model>? = null,
    private val onItemBindCallback: (Binding, Model) -> Unit = { _, _ -> }
) : ListAdapter<Model, InlineViewHolder<Binding, Model>>(ItemsDiffer<Model>().apply {
    itemsCheck = itemsComparator
}) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InlineViewHolder<Binding, Model> {
        val inflater = LayoutInflater.from(context)
        val binding = bindingProducer(inflater, parent)
        return InlineViewHolder(binding, onItemBindCallback)
    }

    override fun onBindViewHolder(holder: InlineViewHolder<Binding, Model>, position: Int) {
        val item = getItem(position)
        holder.onBind(holder.binding, item)

        onItemClickListener?.also { listener ->
            holder.itemView.setOnClickListener {
                listener.onClick(holder.binding, item)
            }

            holder.itemView.setOnLongClickListener {
                listener.onLongClick(holder.binding, item)
                true
            }
        }
    }

    override fun submitList(list: List<Model>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}

interface ItemClickListener<Binding : ViewBinding, T> {

    fun onClick(binding: Binding, item: T)

    fun onLongClick(binding: Binding, item: T)

}

class ItemsDiffer<T> : DiffUtil.ItemCallback<T>() {
    var itemsCheck: (T, T) -> Boolean = { _, _ -> true }
    var contentsCheck: (T, T) -> Boolean = { oldItem, newItem -> oldItem?.equals(newItem) ?: false }

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        itemsCheck(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        contentsCheck(oldItem, newItem)
}