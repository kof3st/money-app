package me.kofesst.android.moneyapp.view

import android.content.res.Resources.getSystem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.util.*
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

    protected open val topBar: MaterialToolbar? get() = null
    protected open val topBarConfig: FragmentTopBarConfig? get() = null

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

        setupTopBar()
    }

    private fun setupTopBar() {
        topBar?.also { bar ->
            topBarConfig?.also { config ->
                config.titleSetter?.also { it(bar) }
                config.subtitleSetter?.also { it(bar) }
                if (config.hasBackButton) {
                    bar.setNavigationOnClickListener { navigateUp() }
                }
            }
        }
    }

    private fun Fragment.viewModel(
        clazz: KClass<FragmentViewModel>,
        ownerProducer: () -> ViewModelStoreOwner = { this },
        factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    ) = createViewModelLazy(clazz, { ownerProducer().viewModelStore }, factoryProducer)
}

data class FragmentTopBarConfig(
    val titleSetter: ((MaterialToolbar) -> Unit)? = null,
    val subtitleSetter: ((MaterialToolbar) -> Unit)? = null,
    val hasBackButton: Boolean = false
)

fun <T> Fragment.observe(stateFlow: StateFlow<T>, block: (T) -> Unit) =
    lifecycleScope.launchWhenStarted { stateFlow collectWith block }

suspend infix fun <T> StateFlow<T>.collectWith(block: (T) -> Unit) =
    this.onEach { block(it) }.collect()

fun Fragment.navigateToShared(
    sharedElements: List<SharedElement>,
    direction: NavDirections
) {
    val extras = sharedElements.extras()
    findNavController().navigate(direction, extras)
}

fun Fragment.navigateUp() =
    findNavController().navigateUp()

fun Fragment.showConfirmDialog(
    @StringRes titleResId: Int,
    @StringRes messageResId: Int,
    @StringRes confirmButtonTextResId: Int,
    confirmButtonClickCallback: () -> Unit
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(titleResId)
        .setMessage(messageResId)
        .setNeutralButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
        .setPositiveButton(confirmButtonTextResId) { _, _ ->
            confirmButtonClickCallback()
        }
        .show()
}

val Int.dp get() = (this / getSystem().displayMetrics.density).toInt()