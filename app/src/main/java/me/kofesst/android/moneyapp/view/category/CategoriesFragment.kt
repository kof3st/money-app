package me.kofesst.android.moneyapp.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCategoriesBinding
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.util.CasesUtil
import me.kofesst.android.moneyapp.view.ExitSharedTransition
import me.kofesst.android.moneyapp.view.FragmentBase
import me.kofesst.android.moneyapp.view.Postpone
import me.kofesst.android.moneyapp.view.navigateToShared
import me.kofesst.android.moneyapp.view.recyclerview.CategoriesAdapter
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.ViewModelFactory
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModel

class CategoriesFragment : FragmentBase<FragmentCategoriesBinding>(), Postpone,
    ExitSharedTransition {
    companion object {
        private const val CATEGORIES_CASES_WORD_UID = "categories_count"
    }

    private val viewModel: CategoriesViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { ViewModelFactory { CategoriesViewModel(requireActivity().application) } }
    )

    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoriesBinding {
        return FragmentCategoriesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupCases()
        setupObserves()

        viewModel.updateCategories()
    }

    private fun setupViews() {
        categoriesAdapter = CategoriesAdapter(requireContext()).apply {
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

        binding.categoriesView.apply {
            adapter = categoriesAdapter
            addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }

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

    private fun setupObserves() {
        lifecycleScope.launchWhenStarted {
            viewModel.categories
                .onEach { categories ->
                    categoriesAdapter.submitList(categories)
                    updateTopBar(categories.size)
                }
                .collect()
        }
    }

    private fun updateTopBar(count: Int) {
        binding.topBar.subtitle = getString(R.string.count_format)
            .format(CasesUtil.getCase(CATEGORIES_CASES_WORD_UID, count))
    }
}