package me.kofesst.android.moneyapp.view.dialog

import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.AddTransactionDialogBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.model.TransactionEntity
import java.lang.Exception

class AddTransactionDialog(
    private val categories: List<CategoryEntity>,
    private val assets: List<AssetEntity>,
    private val asset: AssetEntity,
    private val isTransfer: Boolean = false,
    onSubmit: (TransactionEntity) -> Unit
): ModelDialog<TransactionEntity>(
    saveButtonTextRes = R.string.create,
    contentRes = R.layout.add_transaction_dialog,
    onModelSubmit = onSubmit
) {
    private lateinit var binding : AddTransactionDialogBinding
    private var selectedCategory: CategoryEntity? = null
    private var selectedTarget: AssetEntity? = null

    override fun onCreateContentCreated(contentView: View) {
        binding = AddTransactionDialogBinding.bind(contentView)

        if (isTransfer) {
            binding.titleText.setText("Перевод")
            binding.titleText.isEnabled = false
            binding.categoryTextLayout.visibility = View.GONE
            binding.amountText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL

            val targetsAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                assets
            )
            binding.targetText.setAdapter(targetsAdapter)
            binding.targetText.setOnItemClickListener { _, _, position, _ ->
                selectedTarget = assets[position]
            }
        }
        else {
            binding.targetTextLayout.visibility = View.GONE

            val categoriesAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categories
            )
            binding.categoryText.setAdapter(categoriesAdapter)
            binding.categoryText.setOnItemClickListener { _, _, position, _ ->
                selectedCategory = categories[position]
            }
        }
    }

    override fun getModelFromFields(): TransactionEntity? {
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
            assetId = asset.assetId.toLong(),
            targetId = selectedTarget?.assetId?.toLong(),
            categoryName = selectedCategory?.name ?: getString(R.string.transfer),
            assetName = asset.name,
            targetName = selectedTarget?.name,
            title = title!!,
            amount = amount
        )
    }
}