package me.kofesst.android.moneyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.kofesst.android.moneyapp.databinding.EmptySourceViewBinding
import me.kofesst.android.moneyapp.view.recyclerview.InlineAdapter
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase
import kotlin.reflect.KClass

abstract class ListFragmentBase<FragmentBinding : ViewBinding,
        FragmentViewModel : ViewModelBase,
        Model,
        ItemBinding : ViewBinding>(
    viewModelClass: KClass<FragmentViewModel>
) : FragmentBase<FragmentBinding, FragmentViewModel>(viewModelClass) {
    protected abstract val viewHolderBindingProducer: (LayoutInflater, ViewGroup) -> ItemBinding
    protected abstract val onViewHolderBindCallback: (ItemBinding, Model) -> Unit
    protected abstract val itemsComparator: (Model, Model) -> Boolean
    protected abstract val listStateFlow: StateFlow<List<Model>>
    protected abstract val emptySourceView: EmptySourceViewBinding

    protected open val divider: RecyclerView.ItemDecoration? get() = null
    protected open val onItemClickListener: ItemClickListener<Model>? get() = null

    private lateinit var fragmentAdapter: InlineAdapter<ItemBinding, Model>

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

    private fun createAdapter(): InlineAdapter<ItemBinding, Model> =
        InlineAdapter(
            context = requireContext(),
            bindingProducer = { inflater, parent -> viewHolderBindingProducer(inflater, parent) },
            itemsComparator = itemsComparator,
            onItemClickListener = onItemClickListener,
            onItemBindCallback = onViewHolderBindCallback
        )

    private fun setupObserves() {
        lifecycleScope.launchWhenStarted {
            listStateFlow.onEach { models ->
                fragmentAdapter.submitList(models)
                onListObserved(models)

                if (models.isEmpty()) {
                    emptySourceView.root.visibility = View.VISIBLE
                    emptySourceView.image.playAnimation()
                } else {
                    emptySourceView.root.visibility = View.GONE
                    emptySourceView.image.cancelAnimation()
                }
            }.collect()
        }
    }
}