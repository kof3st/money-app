package me.kofesst.android.moneyapp.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.CategoryItemBinding
import me.kofesst.android.moneyapp.databinding.EmptySourceViewBinding
import me.kofesst.android.moneyapp.databinding.FragmentCategoriesBinding
import me.kofesst.android.moneyapp.databinding.SourceViewBinding
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.util.CasesUtil
import me.kofesst.android.moneyapp.util.shared
import me.kofesst.android.moneyapp.view.*
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModel

class CategoriesFragment :
    ListFragmentBase<FragmentCategoriesBinding, CategoriesViewModel, CategoryEntity, CategoryItemBinding>(
        CategoriesViewModel::class
    ), Postpone, ExitSharedTransition {
    companion object {
        private const val CATEGORIES_CASES_WORD_UID = "categories_count"
    }

    override val itemLayoutResId: Int
        get() = R.layout.category_item

    override val viewHolderBindingProducer: (View) -> CategoryItemBinding
        get() = { CategoryItemBinding.bind(it) }

    override val onViewHolderBindCallback: (CategoryItemBinding, CategoryEntity) -> Unit
        get() = { binding, item -> binding.nameText.text = item.name }

    override val itemsComparator: (CategoryEntity, CategoryEntity) -> Boolean
        get() = { first, second -> first.categoryId == second.categoryId }

    override val emptySourceView: EmptySourceViewBinding
        get() = binding.emptySourceView

    override val sourceView: SourceViewBinding
        get() = binding.sourceView

    override val divider: RecyclerView.ItemDecoration?
        get() = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )

    override val itemTransitionConfig: ItemTransitionConfig<CategoryItemBinding, CategoryEntity>
        get() = ItemTransitionConfig(
            directionProducer = { CategoriesFragmentDirections.actionCategoryDetails(it) },
            itemIdProducer = { it.categoryId.toString() }
        )

    override fun createViewModel(): CategoriesViewModel =
        CategoriesViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoriesBinding {
        return FragmentCategoriesBinding.inflate(inflater, container, false)
    }

    override fun onListObserved(list: List<CategoryEntity>) = updateTopBar(list.size)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupCases()

        viewModel.updateItems()
    }

    private fun setupViews() {
        binding.newCategoryButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    listOf(button shared R.string.edit_shared_transition_name),
                    CategoriesFragmentDirections.actionCreateCategory()
                )
            }
        }
    }

    private fun setupCases() {
        CasesUtil.registerWord(
            uid = CATEGORIES_CASES_WORD_UID,
            firstCase = "категория",
            secondCase = "категории",
            thirdCase = "категорий"
        )
    }

    private fun updateTopBar(count: Int) {
        binding.topBar.subtitle = getString(R.string.count_format)
            .format(CasesUtil.getCase(CATEGORIES_CASES_WORD_UID, count))
    }
}