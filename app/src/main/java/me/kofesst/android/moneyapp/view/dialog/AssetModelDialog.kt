package me.kofesst.android.moneyapp.view.dialog

import android.view.View
import android.widget.ArrayAdapter
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.AssetModelDialogBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.format
import java.lang.Exception

class AssetModelDialog(
    onAssetSubmit: (AssetEntity) -> Unit,
    private val editingModel: AssetEntity? = null
): ModelDialog<AssetEntity>(
    titleRes = if (editingModel == null) R.string.new_asset else R.string.edit_asset,
    saveButtonTextRes = if (editingModel == null) R.string.create else R.string.edit,
    contentRes = R.layout.asset_model_dialog,
    onModelSubmit = onAssetSubmit
) {
    private lateinit var binding : AssetModelDialogBinding
    private var selectedType: AssetTypes? = null

    override fun onCreateContentCreated(contentView: View) {
        binding = AssetModelDialogBinding.bind(contentView)

        val typesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            AssetTypes.values().map { getString(it.titleRes) }
        )
        binding.typeText.setAdapter(typesAdapter)
        binding.typeText.setOnItemClickListener { _, _, position, _ ->
            selectedType = AssetTypes.values()[position]
        }

        if (editingModel != null) {
            binding.nameText.setText(editingModel.name)
            binding.balanceText.setText(editingModel.balance.format())
            binding.typeText.setSelection(editingModel.type)
        }
    }

    override fun getModelFromFields(): AssetEntity? {
        binding.nameTextLayout.error = null
        binding.balanceTextLayout.error = null
        binding.typeTextLayout.error = null

        var error = false

        val name = binding.nameText.text?.toString()
        if (name == null || name.trim().isEmpty()) {
            binding.nameTextLayout.error = getString(R.string.error_required)
            error = true
        }
        else if (name.length > binding.nameTextLayout.counterMaxLength) {
            binding.nameTextLayout.error = getString(R.string.error_counter)
            error = true
        }

        if (selectedType == null) {
            binding.typeTextLayout.error = getString(R.string.error_required)
            error = true
        }

        val balanceStr = binding.balanceText.text?.toString()
        if (balanceStr == null || balanceStr.trim().isEmpty()) {
            binding.balanceTextLayout.error = getString(R.string.error_required)
            error = true
        }

        if (error) return null

        var balance: Double
        try {
            balance = balanceStr!!.toDouble()
        } catch(exception: Exception) {
            binding.balanceTextLayout.error = getString(R.string.error_incorrect)
            return null
        }

        if (balance > 1_000_000_000) {
            binding.balanceTextLayout.error = getString(R.string.error_big)
            return null
        }

        if (balance < -1_000_000_000) {
            binding.balanceTextLayout.error = getString(R.string.error_small)
            return null
        }

        return AssetEntity(
            name = name!!,
            balance = balance,
            type = selectedType!!.ordinal
        )
    }
}