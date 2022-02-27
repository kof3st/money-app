package me.kofesst.android.moneyapp.view.dialog

import android.view.View
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.CategoryMenuDialogBinding
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.util.showDeleteDialogWithSnackbar
import me.kofesst.android.moneyapp.viewmodel.CategoriesViewModel

class CategoryMenuDialog(
    private val parent: View,
    private val viewModel: CategoriesViewModel,
    private val item: CategoryEntity
): BottomSheetDialog(R.layout.category_menu_dialog) {
    private lateinit var binding: CategoryMenuDialogBinding

    override fun onContentCreated(contentView: View) {
        binding = CategoryMenuDialogBinding.bind(contentView)

        binding.editButton.setOnClickListener {
            val modelDialog = CategoryModelDialog(
                editingModel = item,
                onCategorySubmit = {
                    item.name = it.name
                    viewModel.updateCategory(item)
                }
            )
            modelDialog.show(parentFragmentManager, "create_category_dialog")
        }

        binding.deleteButton.setOnClickListener {
            showDeleteDialogWithSnackbar(
                fragmentView = parent,
                dialogTitleRes = R.string.confirm_dialog_title,
                dialogMessageRes = R.string.delete_category_message,
                snakbarMessageRes = R.string.snackbar_category_deleted,
                deleteAction = { viewModel.deleteCategory(item) },
                undoAction = { viewModel.addCategory(item) }
            )

            dismiss()
        }
    }
}