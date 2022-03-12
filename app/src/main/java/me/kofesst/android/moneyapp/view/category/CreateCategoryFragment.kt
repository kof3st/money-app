package me.kofesst.android.moneyapp.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCreateCategoryBinding
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.view.EnterSharedTransition
import me.kofesst.android.moneyapp.view.FragmentBase
import me.kofesst.android.moneyapp.view.navigateUp
import me.kofesst.android.moneyapp.viewmodel.ViewModelFactory
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModel

class CreateCategoryFragment : FragmentBase<FragmentCreateCategoryBinding>(),
    EnterSharedTransition {
    private val viewModel: CategoriesViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { ViewModelFactory { CategoriesViewModel(requireActivity().application) } }
    )

    private val args: CreateCategoryFragmentArgs by navArgs()
    private val editing by lazy { args.editing }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateCategoryBinding {
        return FragmentCreateCategoryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopBar()
        setupViews()
    }

    private fun setupTopBar() {
        binding.topBar.apply {
            if (editing != null) setTitle(R.string.edit_category)
            setNavigationOnClickListener { navigateUp() }
        }
    }

    private fun setupViews() {
        if (editing != null) binding.nameText.setText(editing!!.name)

        binding.saveButton.apply {
            if (editing != null) setText(R.string.edit_category)

            setOnClickListener {
                val category = getModelFromFields() ?: return@setOnClickListener
                if (editing != null) {
                    editing!!.name = category.name
                    viewModel.addCategory(editing!!)
                } else {
                    viewModel.addCategory(category)
                }

                navigateUp()
            }
        }
    }

    private fun getModelFromFields(): CategoryEntity? {
        binding.nameTextLayout.error = null

        val name = binding.nameText.text?.toString()
        if (name == null || name.trim().isEmpty()) {
            binding.nameTextLayout.error = getString(R.string.error_required)
            return null
        } else if (name.length > binding.nameTextLayout.counterMaxLength) {
            binding.nameTextLayout.error = getString(R.string.error_counter)
            return null
        }

        return CategoryEntity(
            name = name
        )
    }
}