package me.kofesst.android.moneyapp.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import me.kofesst.android.moneyapp.databinding.AssetItemBinding
import me.kofesst.android.moneyapp.model.AssetEntity

class AssetsAdapter(
    private val context: Context
): ListAdapter<AssetEntity, AssetViewHolder>(AssetsDiffer) {
    private var onItemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val inflater = LayoutInflater.from(context)
        val viewHolderBinding = AssetItemBinding.inflate(inflater, parent, false)
        return AssetViewHolder(viewHolderBinding)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onClick(item)
        }

        holder.itemView.setOnLongClickListener {
            onItemClickListener?.onLongClick(item)
            true
        }
    }

    fun addOnItemClickListener(listener: ItemClickListener) {
        onItemClickListener = listener
    }

    override fun submitList(list: List<AssetEntity>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}

interface ItemClickListener {

    fun onClick(item: AssetEntity)

    fun onLongClick(item: AssetEntity)

}

object AssetsDiffer: DiffUtil.ItemCallback<AssetEntity>() {

    override fun areItemsTheSame(oldItem: AssetEntity, newItem: AssetEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AssetEntity, newItem: AssetEntity): Boolean {
        return oldItem.balance == newItem.balance &&
                oldItem.name == newItem.name &&
                oldItem.type == newItem.type
    }

}