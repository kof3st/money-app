package me.kofesst.android.moneyapp.view.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCategoryDetailsBinding
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.util.include
import me.kofesst.android.moneyapp.util.setEnterSharedTransition
import me.kofesst.android.moneyapp.util.setExitSharedTransition
import me.kofesst.android.moneyapp.util.showDeleteDialogWithSnackbar
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModel
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModelFactory

class CategoryDetailsFragment : Fragment() {
    private val viewModel: CategoriesViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { CategoriesViewModelFactory(requireActivity().application) }
    )

    private lateinit var binding: FragmentCategoryDetailsBinding
    private lateinit var targetCategory: CategoryEntity

    private val args: CategoryDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEnterSharedTransition(R.integer.shared_transition_duration_short)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        targetCategory = args.targetCategory
        setExitSharedTransition(R.integer.shared_transition_duration_short)

        setupTopBar()
        setupActions()
    }

    private fun setupTopBar() {
        binding.topBar.apply {
            title = targetCategory.name
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupActions() {
        binding.editButton.apply {
            setOnClickListener { button ->
                val extras = R.string.edit_shared_transition_name include button
                val direction =
                    CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToCreateCategoryFragment(targetCategory)
                findNavController().navigate(direction, extras)
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
                        viewModel.deleteCategory(targetCategory)
                        findNavController().navigateUp()
                    },
                    undoAction = {
                        viewModel.addCategory(targetCategory)
                    }
                )
            }
        }
    }
}