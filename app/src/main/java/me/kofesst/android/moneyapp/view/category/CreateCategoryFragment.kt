package me.kofesst.android.moneyapp.view.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCreateCategoryBinding
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.util.setEnterSharedTransition
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModel
import me.kofesst.android.moneyapp.viewmodel.category.CategoriesViewModelFactory

class CreateCategoryFragment : Fragment() {
    private val viewModel: CategoriesViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { CategoriesViewModelFactory(requireActivity().application) }
    )

    private lateinit var binding: FragmentCreateCategoryBinding
    private var editing: CategoryEntity? = null

    private val args: CreateCategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEnterSharedTransition(R.integer.shared_transition_duration_short)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editing = args.editing

        setupTopBar()
        setupViews()
    }

    private fun setupTopBar() {
        binding.topBar.apply {
            if (editing != null) {
                setTitle(R.string.edit_category)
            }

            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupViews() {
        if (editing != null) {
            binding.nameText.setText(editing!!.name)
        }

        binding.saveButton.apply {
            if (editing != null) {
                setText(R.string.edit_category)
            }

            setOnClickListener {
                val category = getModelFromFields() ?: return@setOnClickListener
                if (editing != null) {
                    editing!!.name = category.name
                    viewModel.addCategory(editing!!)
                }
                else {
                    viewModel.addCategory(category)
                }

                findNavController().navigateUp()
            }
        }
    }

    private fun getModelFromFields(): CategoryEntity? {
        binding.nameTextLayout.error = null

        val name = binding.nameText.text?.toString()
        if (name == null || name.trim().isEmpty()) {
            binding.nameTextLayout.error = getString(R.string.error_required)
            return null
        }
        else if (name.length > binding.nameTextLayout.counterMaxLength) {
            binding.nameTextLayout.error = getString(R.string.error_counter)
            return null
        }

        return CategoryEntity(
            name = name
        )
    }
}