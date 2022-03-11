package me.kofesst.android.moneyapp.view.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<M>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: M)

}