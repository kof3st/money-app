package me.kofesst.android.moneyapp.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.kofesst.android.moneyapp.view.recyclerview.BaseAdapter
import me.kofesst.android.moneyapp.view.recyclerview.BaseViewHolder
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase
import kotlin.reflect.KClass

abstract class ListFragmentBase<FragmentBinding : ViewBinding,
        FragmentViewModel : ViewModelBase,
        Model,
        ViewHolder : BaseViewHolder<Model>,
        Adapter : BaseAdapter<Model, ViewHolder>>(
    viewModelClass: KClass<FragmentViewModel>
) : FragmentBase<FragmentBinding, FragmentViewModel>(viewModelClass) {
    protected open val divider: RecyclerView.ItemDecoration? get() = null
    protected open val listStateFlow: StateFlow<List<Model>>? get() = null

    private lateinit var fragmentAdapter: Adapter

    protected abstract fun createAdapter(): Adapter
    protected abstract fun getRecyclerView(): RecyclerView

    protected open fun onListObserved(list: List<Model>) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObserves()
    }

    private fun setupRecyclerView() {
        fragmentAdapter = createAdapter()
        getRecyclerView().apply {
            adapter = fragmentAdapter
            divider?.also { addItemDecoration(it) }
        }
    }

    private fun setupObserves() {
        lifecycleScope.launchWhenStarted {
            listStateFlow?.also {
                it.onEach { models ->
                    fragmentAdapter.submitList(models)
                    onListObserved(models)
                }.collect()
            }
        }
    }
}