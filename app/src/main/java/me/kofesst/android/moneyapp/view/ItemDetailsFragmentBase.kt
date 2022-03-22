package me.kofesst.android.moneyapp.view

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.viewbinding.ViewBinding
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase
import kotlin.reflect.KClass

abstract class ItemDetailsFragmentBase<FragmentBinding : ViewBinding, FragmentViewModel : ViewModelBase>(
    viewModelClass: KClass<FragmentViewModel>
) : FragmentBase<FragmentBinding, FragmentViewModel>(viewModelClass) {
    protected abstract val itemId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setTransitionName(
            binding.root,
            getString(R.string.item_details_transition_name).format(itemId)
        )
    }
}