package me.kofesst.android.moneyapp.view.dialog

import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.AssetMenuDialogBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.util.showDeleteDialogWithSnackbar
import me.kofesst.android.moneyapp.viewmodel.AssetsViewModel

class AssetMenuDialog(
    private val parent: View,
    private val viewModel: AssetsViewModel,
    private val item: AssetEntity
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
                    viewModel.updateAsset(item)
                }
            )
            modelDialog.show(parentFragmentManager, "create_asset_dialog")
        }

        binding.transactionButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val transactionDialog = AddTransactionDialog(
                    categories = viewModel.getCategories(),
                    assets = listOf(),
                    asset = item
                ) {
                    item.balance += it.amount
                    viewModel.updateAsset(item)

                    viewModel.addTransaction(it)
                    dismiss()
                }
                transactionDialog.show(parentFragmentManager, "add_transaction_dialog")
            }
        }

        binding.transferButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val transactionDialog = AddTransactionDialog(
                    categories = listOf(),
                    assets = viewModel.assetsLiveData.value!!.filter { it.assetId != item.assetId },
                    asset = item,
                    isTransfer = true
                ) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        item.balance -= it.amount
                        viewModel.updateAsset(item)

                        val target = viewModel.getAsset(it.targetId!!)!!
                        target.balance += it.amount

                        Log.d("AAA", "$item asset")
                        Log.d("AAA", "$target target")

                        viewModel.updateAsset(target)

                        viewModel.addTransaction(it)
                        dismiss()
                    }
                }
                transactionDialog.show(parentFragmentManager, "add_transaction_dialog")
            }
        }

        binding.deleteButton.setOnClickListener {
            showDeleteDialogWithSnackbar(
                fragmentView = parent,
                dialogTitleRes = R.string.confirm_dialog_title,
                dialogMessageRes = R.string.delete_asset_message,
                snakbarMessageRes = R.string.snackbar_asset_deleted,
                deleteAction = { viewModel.deleteAsset(item) },
                undoAction = { viewModel.addAsset(item) }
            )

            dismiss()
        }
    }
}