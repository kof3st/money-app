package me.kofesst.android.moneyapp.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCategoryDetailsBinding
import me.kofesst.android.moneyapp.util.showDeleteDialogWithSnackbar
import me.kofesst.android.moneyapp.view.*
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModel

class CategoryDetailsFragment : FragmentBase<FragmentCategoryDetailsBinding, CategoriesViewModel>(
    CategoriesViewModel::class
), EnterSharedTransition, ExitSharedTransition {
    private val args: CategoryDetailsFragmentArgs by navArgs()
    private val category by lazy { args.targetCategory }

    override fun createViewModel(): CategoriesViewModel =
        CategoriesViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryDetailsBinding {
        return FragmentCategoryDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopBar()
        setupActions()
    }

    private fun setupTopBar() {
        binding.topBar.apply {
            title = category.name
            setNavigationOnClickListener { navigateUp() }
        }
    }

    private fun setupActions() {
        binding.editButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    R.string.edit_shared_transition_name,
                    button, CategoryDetailsFragmentDirections.actionEditCategory(category)
                )
            }
        }

        binding.deleteButton.apply {
            setOnClickListener {
                showDeleteDialogWithSnackbar(
                    fragmentView = requireActivity().findViewById(R.id.fragment_container),
                    dialogTitleRes = R.string.confirm_dialog_title,
                    dialogMessageRes = R.string.delete_category_message,
                    snackbarMessageRes = R.string.snackbar_category_deleted,
                    deleteAction = {
                        viewModel.deleteCategory(category)
                        navigateUp()
                    },
                    undoAction = {
                        viewModel.addCategory(category)
                    }
                )
            }
        }
    }
}