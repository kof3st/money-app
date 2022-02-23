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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val inflater = LayoutInflater.from(context)
        val viewHolderBinding = AssetItemBinding.inflate(inflater, parent, false)
        return AssetViewHolder(viewHolderBinding)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

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