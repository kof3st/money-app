package me.kofesst.android.moneyapp.view.dialog

import android.view.View
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.AssetMenuDialogBinding
import me.kofesst.android.moneyapp.model.AssetEntity

class AssetMenuDialog: BottomSheetDialog(R.string.asset_menu_title, R.layout.asset_menu_dialog) {
    private lateinit var binding: AssetMenuDialogBinding

    var onEditClickCallback: (() -> Unit)? = null
    var onTransactionClickCallback: (() -> Unit)? = null
    var onDeleteClickCallback: (() -> Unit)? = null

    override fun onContentCreated(contentView: View) {
        binding = AssetMenuDialogBinding.bind(contentView)

        binding.editButton.setOnClickListener {
            onEditClickCallback?.invoke()
            dismiss()
        }

        binding.transactionButton.setOnClickListener {
            onTransactionClickCallback?.invoke()
            dismiss()
        }

        binding.deleteButton.setOnClickListener {
            onDeleteClickCallback?.invoke()
            dismiss()
        }
    }
}