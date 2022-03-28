package me.kofesst.android.moneyapp.view.recyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import me.kofesst.android.moneyapp.view.AsyncView

@Suppress("UNCHECKED_CAST")
class InlineAdapter<Binding : ViewBinding, Model>(
    private val context: Context,
    @LayoutRes private val itemLayoutResId: Int,
    private val bindingProducer: (View) -> Binding,
    itemsComparator: (Model, Model) -> Boolean = { _, _ -> true },
    private val onItemClickListener: ItemClickListener<Binding, Model>? = null,
    private val onItemBindCallback: (Binding, Model) -> Unit = { _, _ -> }
) : ListAdapter<Model, InlineViewHolder>(ItemsDiffer<Model>().apply {
    itemsCheck = itemsComparator
}) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InlineViewHolder {
        val view = AsyncView(
            context = context,
            layoutIdRes = itemLayoutResId,
            bindingProducer = bindingProducer
        ).apply {
            inflate()
        }

        return InlineViewHolder(view)
    }

    override fun onBindViewHolder(holder: InlineViewHolder, position: Int) {
        val item = getItem(position)

        (holder.itemView as AsyncView<Binding>).onBindCallback = { binding ->
            onItemBindCallback(binding, item)

            onItemClickListener?.also { listener ->
                binding.root.setOnClickListener {
                    listener.onClick(binding, item)
                }

                binding.root.setOnLongClickListener {
                    listener.onLongClick(binding, item)
                    true
                }
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