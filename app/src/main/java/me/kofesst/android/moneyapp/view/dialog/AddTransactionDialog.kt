package me.kofesst.android.moneyapp.view.dialog

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
    private val asset: AssetEntity,
    onSubmit: (TransactionEntity) -> Unit
): ModelDialog<TransactionEntity>(
    saveButtonTextRes = R.string.create,
    contentRes = R.layout.add_transaction_dialog,
    onModelSubmit = onSubmit
) {
    private lateinit var binding : AddTransactionDialogBinding
    private var selectedCategory: CategoryEntity? = null

    override fun onCreateContentCreated(contentView: View) {
        binding = AddTransactionDialogBinding.bind(contentView)

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

        if (selectedCategory == null) {
            binding.categoryTextLayout.error = getString(R.string.error_required)
            error = true
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
            categoryId = selectedCategory!!.categoryId.toLong(),
            assetId = asset.assetId.toLong(),
            categoryName = selectedCategory!!.name,
            assetName = asset.name,
            title = title!!,
            amount = amount
        )
    }
}