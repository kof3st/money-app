package me.kofesst.android.moneyapp.view.util

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.kofesst.android.moneyapp.model.CategoryEntity

class CategoryModelDialog(
    private val editingModel: CategoryEntity? = null
) : BottomSheetDialogFragment() {
//    saveButtonTextRes = if (editingModel == null) R.string.create else R.string.edit,
//    contentRes = R.layout.category_model_dialog,
//    onModelSubmit = onCategorySubmit
//) {
//    private lateinit var binding: CategoryModelDialogBinding
//
//    override fun onCreateContentCreated(contentView: View) {
//        binding = CategoryModelDialogBinding.bind(contentView)
//
//        if (editingModel != null) {
//            binding.nameText.setText(editingModel.name)
//        }
//    }
//
//    override fun getModelFromFields(): CategoryEntity? {
//        binding.nameTextLayout.error = null
//
//        val name = binding.nameText.text?.toString()
//        if (name == null || name.trim().isEmpty()) {
//            binding.nameTextLayout.error = getString(R.string.error_required)
//            return null
//        }
//        else if (name.length > binding.nameTextLayout.counterMaxLength) {
//            binding.nameTextLayout.error = getString(R.string.error_counter)
//            return null
//        }
//
//        return CategoryEntity(
//            name = name
//        )
//    }
}