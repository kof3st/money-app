package me.kofesst.android.moneyapp.view.dialog

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.ModelBottomDialogBinding

abstract class ModelDialog<M>(
    @LayoutRes private val contentRes: Int,
    @StringRes private val saveButtonTextRes: Int,
    private val onModelSubmit: (M) -> Unit
): BottomSheetDialog(R.layout.model_bottom_dialog) {
    private lateinit var binding: ModelBottomDialogBinding

    override fun onContentCreated(contentView: View) {
        binding = ModelBottomDialogBinding.bind(contentView)

        val createContentView = LayoutInflater.from(requireContext())
            .inflate(contentRes, null, false)
        binding.createContent.addView(createContentView)

        onCreateContentCreated(createContentView)

        binding.saveButton.apply {
            setText(saveButtonTextRes)
            setOnClickListener {
                val model = getModelFromFields() ?: return@setOnClickListener
                onModelSubmit(model)

                dismiss()
            }
        }
    }

    abstract fun getModelFromFields(): M?

    protected open fun onCreateContentCreated(contentView: View) { }
}