package me.kofesst.android.moneyapp.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.kofesst.android.moneyapp.databinding.BottomDialogBinding

abstract class BottomSheetDialog(
    @LayoutRes private val contentRes: Int
): BottomSheetDialogFragment() {
    private lateinit var binding: BottomDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val contentView = LayoutInflater.from(requireContext())
            .inflate(contentRes, null, false)
        binding.dialogContent.addView(contentView)

        onContentCreated(contentView)
    }

    protected open fun onContentCreated(contentView: View) { }
}