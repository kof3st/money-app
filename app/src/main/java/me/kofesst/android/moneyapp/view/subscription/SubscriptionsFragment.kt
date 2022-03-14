package me.kofesst.android.moneyapp.view.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentSubscriptionsBinding
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.view.ExitSharedTransition
import me.kofesst.android.moneyapp.view.FragmentBase
import me.kofesst.android.moneyapp.view.Postpone
import me.kofesst.android.moneyapp.view.navigateToShared
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.view.recyclerview.SubscriptionsAdapter
import me.kofesst.android.moneyapp.viewmodel.ViewModelFactory
import me.kofesst.android.moneyapp.viewmodel.subscription.SubscriptionsViewModel

class SubscriptionsFragment : FragmentBase<FragmentSubscriptionsBinding>(), Postpone,
    ExitSharedTransition {
    private val viewModel: SubscriptionsViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { ViewModelFactory { SubscriptionsViewModel(requireActivity().application) } }
    )

    private lateinit var subscriptionsAdapter: SubscriptionsAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubscriptionsBinding {
        return FragmentSubscriptionsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObserves()

        viewModel.updateSubscriptions()
    }

    private fun setupViews() {
        subscriptionsAdapter = SubscriptionsAdapter(requireContext()).apply {
            itemClickListener = object : ItemClickListener<SubscriptionEntity> {
                override fun onClick(view: View, item: SubscriptionEntity) {
                    navigateToShared(
                        R.string.edit_shared_transition_name,
                        binding.newSubscriptionButton,
                        SubscriptionsFragmentDirections.actionCreateSubscription(item)
                    )
                }

                override fun onLongClick(view: View, item: SubscriptionEntity) {}
            }
        }
        binding.subscriptionsView.apply {
            adapter = subscriptionsAdapter
            addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        binding.newSubscriptionButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    R.string.edit_shared_transition_name,
                    button,
                    SubscriptionsFragmentDirections.actionCreateSubscription()
                )
            }
        }
    }

    private fun setupObserves() {
        lifecycleScope.launchWhenStarted {
            viewModel.subscriptions
                .onEach { subscriptions ->
                    subscriptionsAdapter.submitList(subscriptions)
                }
                .collect()
        }
    }
}