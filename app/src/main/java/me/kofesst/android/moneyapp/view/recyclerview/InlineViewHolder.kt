package me.kofesst.android.moneyapp.view.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class InlineViewHolder<Binding : ViewBinding, Model>(
    val binding: Binding,
    val onBind: (Binding, Model) -> Unit = { _, _ -> }
) : RecyclerView.ViewHolder(binding.root)