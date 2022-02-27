package me.kofesst.android.moneyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCategoriesBinding
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.util.CasesUtil
import me.kofesst.android.moneyapp.view.recyclerview.CategoriesAdapter
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.CategoriesViewModel
import me.kofesst.android.moneyapp.viewmodel.factory.CategoriesViewModelFactory

class CategoriesFragment : Fragment() {
    companion object {
        private const val CATEGORIES_CASES_WORD_UID = "categories_count"
    }

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var viewModel: CategoriesViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModel()
        setupViews()
        setupCases()
        setupObserves()

        viewModel.updateCategories()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this,
            factory = CategoriesViewModelFactory(requireActivity().application)
        )[CategoriesViewModel::class.java]
    }

    private fun setupViews() {
        categoriesAdapter = CategoriesAdapter(requireContext()).apply {
            itemClickListener = object: ItemClickListener<CategoryEntity> {
                override fun onClick(item: CategoryEntity) { }

                override fun onLongClick(item: CategoryEntity) { }
            }
        }

        binding.categoriesView.apply {
            adapter = categoriesAdapter
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
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) {
            categoriesAdapter.submitList(it.toList())
            updateTopBar(it.size)
        }
    }

    private fun updateTopBar(amount: Int) {
        binding.topBar.subtitle = getString(R.string.categories_top_bar_format)
            .format(CasesUtil.getCase(CATEGORIES_CASES_WORD_UID, amount))
    }
}