package me.kofesst.android.moneyapp.view.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.StateFlow
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentSubscriptionsBinding
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.view.ExitSharedTransition
import me.kofesst.android.moneyapp.view.ListFragmentBase
import me.kofesst.android.moneyapp.view.Postpone
import me.kofesst.android.moneyapp.view.navigateToShared
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.view.recyclerview.SubscriptionViewHolder
import me.kofesst.android.moneyapp.view.recyclerview.SubscriptionsAdapter
import me.kofesst.android.moneyapp.viewmodel.subscription.SubscriptionsViewModel

class SubscriptionsFragment : ListFragmentBase<FragmentSubscriptionsBinding,
        SubscriptionsViewModel,
        SubscriptionEntity,
        SubscriptionViewHolder,
        SubscriptionsAdapter>(
    SubscriptionsViewModel::class
), Postpone,
    ExitSharedTransition {
    override val divider: RecyclerView.ItemDecoration
        get() = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )

    override val listStateFlow: StateFlow<List<SubscriptionEntity>>
        get() = viewModel.subscriptions

    override fun createViewModel(): SubscriptionsViewModel =
        SubscriptionsViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubscriptionsBinding {
        return FragmentSubscriptionsBinding.inflate(inflater, container, false)
    }

    override fun createAdapter(): SubscriptionsAdapter =
        SubscriptionsAdapter(requireContext()).apply {
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

    override fun getRecyclerView(): RecyclerView = binding.subscriptionsView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        viewModel.updateSubscriptions()
    }

    private fun setupViews() {
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
}