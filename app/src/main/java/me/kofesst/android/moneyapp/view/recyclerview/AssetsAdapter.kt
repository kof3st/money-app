package me.kofesst.android.moneyapp.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import me.kofesst.android.moneyapp.databinding.AssetItemBinding
import me.kofesst.android.moneyapp.model.AssetEntity

class AssetsAdapter(
    private val context: Context
): BaseAdapter<AssetEntity, AssetViewHolder>(DIFFER) {
    companion object {
        private val DIFFER = ItemsDiffBuilder<AssetEntity>().apply {
            itemsCheck = { old, new -> old.assetId == new.assetId }
            contentsCheck = { old, new ->
                old.name == new.name &&
                old.balance == new.balance &&
                old.type == new.type
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val inflater = LayoutInflater.from(context)
        val viewHolderBinding = AssetItemBinding.inflate(inflater, parent, false)
        return AssetViewHolder(viewHolderBinding)
    }
}