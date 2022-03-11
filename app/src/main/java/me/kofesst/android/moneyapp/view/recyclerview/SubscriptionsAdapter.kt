package me.kofesst.android.moneyapp.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import me.kofesst.android.moneyapp.databinding.SubscriptionItemBinding
import me.kofesst.android.moneyapp.model.SubscriptionEntity

class SubscriptionsAdapter(
    private val context: Context
) : BaseAdapter<SubscriptionEntity, SubscriptionViewHolder>(DIFFER) {
    companion object {
        private val DIFFER = ItemsDiffBuilder<SubscriptionEntity>().apply {
            itemsCheck = { old, new -> old.subscriptionId == new.subscriptionId }
            contentsCheck = { old, new ->
                old.title == new.title &&
                        old.amount == new.amount &&
                        old.type == new.type &&
                        old.day == new.day
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val inflater = LayoutInflater.from(context)
        val viewHolderBinding = SubscriptionItemBinding.inflate(inflater, parent, false)
        return SubscriptionViewHolder(viewHolderBinding)
    }
}