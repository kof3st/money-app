package me.kofesst.android.moneyapp.util

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import me.kofesst.android.moneyapp.R

/**
 * Показывает диалоговое окно,
 * которое создаётся при помощи [MaterialAlertDialogBuilder].
 */
fun Fragment.showDeleteDialogWithSnackbar(
    fragmentView: View,
    @StringRes dialogTitleRes: Int,
    @StringRes dialogMessageRes: Int,
    @StringRes snackbarMessageRes: Int,
    deleteAction: () -> Unit,
    undoAction: () -> Unit
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(dialogTitleRes)
        .setMessage(dialogMessageRes)
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.delete) { _, _ ->
            deleteAction()

            Snackbar.make(fragmentView, snackbarMessageRes, Snackbar.LENGTH_LONG)
                .setAction(R.string.cancel) { undoAction() }
                .show()
        }
        .show()
}