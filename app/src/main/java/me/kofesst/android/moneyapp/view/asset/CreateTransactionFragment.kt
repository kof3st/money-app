package me.kofesst.android.moneyapp.view.asset

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCreateTransactionBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.util.setEnterSharedTransition
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModel
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModelFactory

class CreateTransactionFragment: Fragment() {
    private val viewModel: AssetsViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { AssetsViewModelFactory(requireActivity().application) }
    )

    private lateinit var binding: FragmentCreateTransactionBinding
    private lateinit var targetAsset: AssetEntity
    private var isTransfer: Boolean = false

    private val args: CreateTransactionFragmentArgs by navArgs()

    private lateinit var categories: List<CategoryEntity>
    private lateinit var targets: List<AssetEntity>

    private var selectedTarget: AssetEntity? = null
    private var selectedCategory: CategoryEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEnterSharedTransition(R.integer.shared_transition_duration_short)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        targetAsset = args.targetAsset
        isTransfer = args.isTransfer

        setupViews()
        setupTopBar()
    }

    private fun setupViews() {
        if (isTransfer) {
            binding.root.transitionName = getString(R.string.transfer_transition_name)

            binding.titleText.setText(R.string.transfer)
            binding.titleText.isEnabled = false
            binding.categoryTextLayout.visibility = View.GONE
            binding.amountText.inputType = InputType.TYPE_CLASS_NUMBER

            lifecycleScope.launch(Dispatchers.IO) {
                targets = viewModel.assets.value.filter { it.assetId != targetAsset.assetId }

                val targetsAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    targets
                )

                withContext(Dispatchers.Main) {
                    binding.targetText.setAdapter(targetsAdapter)
                    binding.targetText.setOnItemClickListener { _, _, position, _ ->
                        selectedTarget = targets[position]
                    }
                }
            }
        }
        else {
            binding.root.transitionName = getString(R.string.add_transaction_transition_name)

            binding.targetTextLayout.visibility = View.GONE

            lifecycleScope.launch(Dispatchers.IO) {
                categories = viewModel.getCategories()

                val categoriesAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    categories
                )

                withContext(Dispatchers.Main) {
                    binding.categoryText.setAdapter(categoriesAdapter)
                    binding.categoryText.setOnItemClickListener { _, _, position, _ ->
                        selectedCategory = categories[position]
                    }
                }
            }
        }

        binding.saveButton.apply {
            setOnClickListener {
                val transaction = getModelFromFields() ?: return@setOnClickListener
                viewModel.addTransaction(transaction)

                if (isTransfer && selectedTarget != null) {
                    targetAsset.balance -= transaction.amount
                    viewModel.addAsset(targetAsset)

                    selectedTarget!!.balance += transaction.amount
                    viewModel.addAsset(selectedTarget!!)
                }
                else {
                    targetAsset.balance += transaction.amount
                    viewModel.addAsset(targetAsset)
                }

                findNavController().navigateUp()
            }
        }
    }

    private fun getModelFromFields(): TransactionEntity? {
        binding.titleTextLayout.error = null
        binding.amountTextLayout.error = null
        binding.categoryTextLayout.error = null

        var error = false

        val title = binding.titleText.text?.toString()
        if (title == null || title.trim().isEmpty()) {
            binding.titleTextLayout.error = getString(R.string.error_required)
            error = true
        }
        else if (title.length > binding.titleTextLayout.counterMaxLength) {
            binding.titleTextLayout.error = getString(R.string.error_counter)
            error = true
        }

        if (isTransfer) {
            if (selectedTarget == null) {
                binding.targetTextLayout.error = getString(R.string.error_required)
                error = true
            }
        }
        else {
            if (selectedCategory == null) {
                binding.categoryTextLayout.error = getString(R.string.error_required)
                error = true
            }
        }

        val amountStr = binding.amountText.text?.toString()
        if (amountStr == null || amountStr.trim().isEmpty()) {
            binding.amountTextLayout.error = getString(R.string.error_required)
            error = true
        }

        if (error) return null

        val amount: Double
        try {
            amount = amountStr!!.toDouble()
        } catch(exception: Exception) {
            binding.amountTextLayout.error = getString(R.string.error_incorrect)
            return null
        }

        if (amount > 1_000_000_000) {
            binding.amountTextLayout.error = getString(R.string.error_big)
            return null
        }

        if (amount < -1_000_000_000) {
            binding.amountTextLayout.error = getString(R.string.error_small)
            return null
        }

        return TransactionEntity(
            categoryId = selectedCategory?.categoryId?.toLong(),
            assetId = targetAsset.assetId.toLong(),
            targetId = selectedTarget?.assetId?.toLong(),
            categoryName = selectedCategory?.name ?: getString(R.string.transfer_category),
            assetName = targetAsset.name,
            targetName = selectedTarget?.name,
            title = title!!,
            amount = amount
        )
    }

    private fun setupTopBar() {
        binding.topBar.apply {
            setTitle(
                if (isTransfer) R.string.transfer_action
                else R.string.new_transaction
            )
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}