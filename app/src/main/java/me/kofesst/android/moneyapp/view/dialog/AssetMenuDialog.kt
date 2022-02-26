package me.kofesst.android.moneyapp.view.dialog

import android.view.View
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.AssetMenuDialogBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.util.showDeleteDialogWithSnackbar
import me.kofesst.android.moneyapp.viewmodel.AssetsViewModel

class AssetMenuDialog(
    private val parent: View,
    private val viewModel: AssetsViewModel,
    private val item: AssetEntity,
    private val listPosition: Int
): BottomSheetDialog(R.layout.asset_menu_dialog) {
    private lateinit var binding: AssetMenuDialogBinding

    override fun onContentCreated(contentView: View) {
        binding = AssetMenuDialogBinding.bind(contentView)

        binding.editButton.setOnClickListener {
            val modelDialog = AssetModelDialog(
                editingModel = item,
                onAssetSubmit = {
                    item.name = it.name
                    item.balance = it.balance
                    item.type = it.type
                    // Не робит обновление ресайклера
                    viewModel.updateAsset(item, listPosition)
                }
            )
            modelDialog.show(parentFragmentManager, "create_asset_dialog")
        }

        binding.transactionButton.setOnClickListener {

        }

        binding.deleteButton.setOnClickListener {
            showDeleteDialogWithSnackbar(
                fragmentView = parent,
                dialogTitleRes = R.string.confirm_dialog_title,
                dialogMessageRes = R.string.delete_asset_message,
                snakbarMessageRes = R.string.snackbar_asset_deleted,
                deleteAction = { viewModel.deleteAsset(item) },
                undoAction = { viewModel.addAsset(item, listPosition) }
            )

            dismiss()
        }
    }
}