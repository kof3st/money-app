package me.kofesst.android.moneyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.util.include
import me.kofesst.android.moneyapp.util.setEnterSharedTransition
import me.kofesst.android.moneyapp.util.setExitSharedTransition
import me.kofesst.android.moneyapp.util.setPostpone
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase
import me.kofesst.android.moneyapp.viewmodel.ViewModelFactory
import kotlin.reflect.KClass

abstract class FragmentBase<FragmentBinding : ViewBinding, FragmentViewModel : ViewModelBase>(
    viewModelClass: KClass<FragmentViewModel>
) : Fragment() {
    private var _viewBinding: FragmentBinding? = null
    protected val binding get() = checkNotNull(_viewBinding)

    protected val viewModel: FragmentViewModel by viewModel(
        viewModelClass,
        ownerProducer = { requireActivity() },
        factoryProducer = { ViewModelFactory { createViewModel() } }
    )

    protected abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBinding

    protected abstract fun createViewModel(): FragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (this is EnterSharedTransition) setEnterSharedTransition(R.integer.shared_transition_duration_short)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this is Postpone) setPostpone(view)
        if (this is ExitSharedTransition) setExitSharedTransition(R.integer.shared_transition_duration_short)
    }

    private fun Fragment.viewModel(
        clazz: KClass<FragmentViewModel>,
        ownerProducer: () -> ViewModelStoreOwner = { this },
        factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    ) = createViewModelLazy(clazz, { ownerProducer().viewModelStore }, factoryProducer)
}

fun Fragment.navigateToShared(
    @StringRes transitionNameRes: Int,
    sharedView: View,
    direction: NavDirections
) {
    val extras = transitionNameRes include sharedView
    findNavController().navigate(direction, extras)
}

fun Fragment.navigateUp() =
    findNavController().navigateUp()