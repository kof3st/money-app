package me.kofesst.android.moneyapp.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.StateFlow
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCategoriesBinding
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.util.CasesUtil
import me.kofesst.android.moneyapp.view.ExitSharedTransition
import me.kofesst.android.moneyapp.view.ListFragmentBase
import me.kofesst.android.moneyapp.view.Postpone
import me.kofesst.android.moneyapp.view.navigateToShared
import me.kofesst.android.moneyapp.view.recyclerview.CategoriesAdapter
import me.kofesst.android.moneyapp.view.recyclerview.CategoryViewHolder
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModel

class CategoriesFragment : ListFragmentBase<FragmentCategoriesBinding,
        CategoriesViewModel,
        CategoryEntity,
        CategoryViewHolder,
        CategoriesAdapter>(
    CategoriesViewModel::class
), Postpone, ExitSharedTransition {
    companion object {
        private const val CATEGORIES_CASES_WORD_UID = "categories_count"
    }

    override val divider: RecyclerView.ItemDecoration
        get() = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )

    override val listStateFlow: StateFlow<List<CategoryEntity>>
        get() = viewModel.categories

    override fun createViewModel(): CategoriesViewModel =
        CategoriesViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoriesBinding {
        return FragmentCategoriesBinding.inflate(inflater, container, false)
    }

    override fun createAdapter(): CategoriesAdapter = CategoriesAdapter(requireContext()).apply {
        itemClickListener = object : ItemClickListener<CategoryEntity> {
            override fun onClick(view: View, item: CategoryEntity) {
                navigateToShared(
                    R.string.category_details_transition_name,
                    binding.topBar,
                    CategoriesFragmentDirections.actionCategoryDetails(item)
                )
            }

            override fun onLongClick(view: View, item: CategoryEntity) {}
        }
    }

    override fun getRecyclerView(): RecyclerView = binding.categoriesView

    override fun onListObserved(list: List<CategoryEntity>) = updateTopBar(list.size)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupCases()
        viewModel.updateCategories()
    }

    private fun setupViews() {
        binding.newCategoryButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    R.string.edit_shared_transition_name,
                    button,
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