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

/**
 * Базовый класс всех фрагментов приложения.
 */
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

    /**
     * Возвращает [ViewBinding] фрагмента.
     */
    protected abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBinding

    /**
     * Создаёт [ViewModelBase] фрагмента.
     */
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

    /**
     * Инициализирует [MaterialToolbar] фрагмента.
     */
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

    /**
     * Lazy-функция, создающая [FragmentViewModel] фрагмента.
     */
    private fun Fragment.viewModel(
        clazz: KClass<FragmentViewModel>,
        ownerProducer: () -> ViewModelStoreOwner = { this },
        factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    ) = createViewModelLazy(clazz, { ownerProducer().viewModelStore }, factoryProducer)
}

/**
 * Класс, представляющий настройки [MaterialToolbar]
 * фрагмента.
 */
data class FragmentTopBarConfig(
    val titleSetter: ((MaterialToolbar) -> Unit)? = null,
    val subtitleSetter: ((MaterialToolbar) -> Unit)? = null,
    val hasBackButton: Boolean = false
)

/**
 * Начинает отслеживать [stateFlow] в [lifecycleScope].
 */
fun <T> Fragment.observe(stateFlow: StateFlow<T>, block: (T) -> Unit) =
    lifecycleScope.launchWhenStarted { stateFlow collectWith block }

/**
 * Упрощение отслеживания [StateFlow].
 */
suspend infix fun <T> StateFlow<T>.collectWith(block: (T) -> Unit) =
    this.onEach { block(it) }.collect()

/**
 * Переход на фрагмент [direction],
 * используя sharedElementTransition.
 */
fun Fragment.navigateToShared(
    sharedElements: List<SharedElement>,
    direction: NavDirections
) {
    val extras = sharedElements.extras()
    findNavController().navigate(direction, extras)
}

/**
 * Переходит на фрагмент, который находится выше
 * в иерархии навигации приложения.
 */
fun Fragment.navigateUp() =
    findNavController().navigateUp()

/**
 * Создаёт диалоговое окно с двумя кнопками.
 */
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