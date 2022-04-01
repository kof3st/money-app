package me.kofesst.android.moneyapp.view

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.StateFlow
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.SourceViewBinding
import me.kofesst.android.moneyapp.util.SharedElement
import me.kofesst.android.moneyapp.view.recyclerview.InlineAdapter
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.ListViewModelBase
import kotlin.reflect.KClass

/**
 * Базовый класс всех фрагментов,
 * хранящих список [Model], используя
 * [RecyclerView].
 */
abstract class ListFragmentBase<FragmentBinding : ViewBinding,
        FragmentViewModel : ListViewModelBase<Model>,
        Model,
        ItemBinding : ViewBinding>(
    viewModelClass: KClass<FragmentViewModel>
) : FragmentBase<FragmentBinding, FragmentViewModel>(viewModelClass) {
    protected abstract val itemLayoutResId: Int
    protected abstract val viewHolderBindingProducer: (View) -> ItemBinding
    protected abstract val onViewHolderBindCallback: (ItemBinding, Model) -> Unit
    protected abstract val itemsComparator: (Model, Model) -> Boolean
    protected abstract val sourceView: SourceViewBinding

    protected open val divider: RecyclerView.ItemDecoration?
        get() = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        ).apply {
            dividerInsetStart = resources.getDimensionPixelSize(R.dimen.dividerInsetStart)
        }

    protected open val itemTransitionConfig: ItemTransitionConfig<ItemBinding, Model>?
        get() = null

    protected open val sourceStateFlow: StateFlow<List<Model>>
        get() = viewModel.items

    private lateinit var fragmentAdapter: InlineAdapter<ItemBinding, Model>

    /**
     * Вызывается после обновления [sourceStateFlow].
     */
    protected open fun onListObserved(list: List<Model>) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayout()
        setupRecyclerView()
        setupObserves()
    }

    /**
     * Инициализирует свайп-контейнер.
     */
    private fun setupRefreshLayout() {
        sourceView.refreshLayout.apply {
            setOnRefreshListener {
                viewModel.updateItems {
                    isRefreshing = false
                }
            }
        }
    }

    /**
     * Инициализирует [RecyclerView]
     */
    private fun setupRecyclerView() {
        fragmentAdapter = createAdapter()

        sourceView.source.apply {
            adapter = fragmentAdapter
            divider?.also { addItemDecoration(it) }
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    /**
     * Создаёт [InlineAdapter] для [RecyclerView].
     */
    private fun createAdapter(): InlineAdapter<ItemBinding, Model> =
        InlineAdapter(
            context = requireContext(),
            itemLayoutResId = itemLayoutResId,
            bindingProducer = { view -> viewHolderBindingProducer(view) },
            itemsComparator = itemsComparator,
            onItemClickListener = object : ItemClickListener<ItemBinding, Model> {
                override fun onClick(binding: ItemBinding, item: Model) {
                    itemTransitionConfig?.also { config ->
                        navigateToShared(
                            listOf(
                                *config.sharedElementsProducer(binding).toTypedArray(),
                                binding.root to getString(R.string.item_details_transition_name).format(
                                    config.itemIdProducer(item)
                                )
                            ),
                            config.directionProducer(item)
                        )
                    }
                }

                override fun onLongClick(binding: ItemBinding, item: Model) {}
            },

            onItemBindCallback = { binding, model ->
                onViewHolderBindCallback(binding, model)

                itemTransitionConfig?.also { config ->
                    ViewCompat.setTransitionName(
                        binding.root,
                        getString(R.string.item_details_transition_name).format(
                            config.itemIdProducer(model)
                        )
                    )
                }
            }
        )

    /**
     * Начинает отслеживание [sourceStateFlow].
     */
    private fun setupObserves() {
        observe(sourceStateFlow) { models ->
            fragmentAdapter.submitList(models)
            onListObserved(models)

            if (models.isEmpty()) {
                sourceView.emptySourceView.visibility = View.VISIBLE
                sourceView.emptySourceView.playAnimation()
            } else {
                sourceView.emptySourceView.visibility = View.GONE
                sourceView.emptySourceView.cancelAnimation()
            }
        }
    }
}

/**
 * Класс, представляющий настройки
 * анимаций при нажатии на элемент списка.
 */
data class ItemTransitionConfig<Binding : ViewBinding, Item>(
    val directionProducer: (Item) -> NavDirections,
    val sharedElementsProducer: (Binding) -> List<SharedElement> = { listOf() },
    val itemIdProducer: (Item) -> String = { "" }
)