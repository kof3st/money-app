package me.kofesst.android.moneyapp.view

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.viewbinding.ViewBinding

class AsyncView<Binding : ViewBinding>(
    context: Context,
    @LayoutRes val layoutIdRes: Int,
    private val bindingProducer: (View) -> Binding,
    var onBindCallback: (Binding) -> Unit = {}
) : FrameLayout(context, null, 0, 0) {
    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    private var _isInflated = false

    fun inflate() {
        AsyncLayoutInflater(context).inflate(layoutIdRes, this) { view, _, _ ->
            _isInflated = true

            val binding = bindingProducer(view)
            addView(binding.root)
            onBindCallback(binding)
        }
    }
}