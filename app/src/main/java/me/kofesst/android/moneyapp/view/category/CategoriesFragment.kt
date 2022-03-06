package me.kofesst.android.moneyapp.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCategoriesBinding
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.util.CasesUtil
import me.kofesst.android.moneyapp.util.include
import me.kofesst.android.moneyapp.util.setExitSharedTransition
import me.kofesst.android.moneyapp.util.setPostpone
import me.kofesst.android.moneyapp.view.recyclerview.CategoriesAdapter
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModel
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModelFactory

class CategoriesFragment : Fragment() {
    companion object {
        private const val CATEGORIES_CASES_WORD_UID = "categories_count"
    }

    private val viewModel: CategoriesViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { CategoriesViewModelFactory(requireActivity().application) }
    )

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setPostpone(view)
        setExitSharedTransition(R.integer.shared_transition_duration_short)

        setupViews()
        setupCases()
        setupObserves()

        viewModel.updateCategories()
    }

    private fun setupViews() {
        categoriesAdapter = CategoriesAdapter(requireContext()).apply {
            itemClickListener = object: ItemClickListener<CategoryEntity> {
                override fun onClick(view: View, item: CategoryEntity) {
                    val extras = R.string.category_details_transition_name include binding.topBar
                    val direction = CategoriesFragmentDirections.actionCategoryDetails(item)
                    findNavController().navigate(direction, extras)
                }

                override fun onLongClick(view: View, item: CategoryEntity) { }
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
                val extras = R.string.edit_shared_transition_name include button
                val direction = CategoriesFragmentDirections.actionCreateCategory()
                findNavController().navigate(direction, extras)
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