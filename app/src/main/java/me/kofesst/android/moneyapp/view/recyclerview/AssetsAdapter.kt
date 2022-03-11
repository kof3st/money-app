package me.kofesst.android.moneyapp.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import me.kofesst.android.moneyapp.databinding.AssetItemBinding
import me.kofesst.android.moneyapp.model.AssetWithSubscriptions

class AssetsAdapter(
    private val context: Context
) : BaseAdapter<AssetWithSubscriptions, AssetViewHolder>(DIFFER) {
    companion object {
        private val DIFFER = ItemsDiffBuilder<AssetWithSubscriptions>().apply {
            itemsCheck = { old, new -> old.asset.assetId == new.asset.assetId }
            contentsCheck = { old, new ->
                old.asset.name == new.asset.name &&
                        old.asset.balance == new.asset.balance &&
                        old.asset.type == new.asset.type
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val inflater = LayoutInflater.from(context)
        val viewHolderBinding = AssetItemBinding.inflate(inflater, parent, false)
        return AssetViewHolder(viewHolderBinding)
    }
}