package me.kofesst.android.moneyapp.view.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import me.kofesst.android.moneyapp.databinding.EmptySourceViewBinding
import me.kofesst.android.moneyapp.databinding.FragmentSubscriptionsBinding
import me.kofesst.android.moneyapp.databinding.SourceViewBinding
import me.kofesst.android.moneyapp.databinding.SubscriptionItemBinding
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.model.default.SubscriptionTypes
import me.kofesst.android.moneyapp.util.*
import me.kofesst.android.moneyapp.view.*
import me.kofesst.android.moneyapp.viewmodel.subscription.SubscriptionsViewModel

class SubscriptionsFragment :
    ListFragmentBase<FragmentSubscriptionsBinding, SubscriptionsViewModel, SubscriptionEntity, SubscriptionItemBinding>(
        SubscriptionsViewModel::class
    ), Postpone, ExitSharedTransition {
    override val viewHolderBindingProducer: (LayoutInflater, ViewGroup) -> SubscriptionItemBinding
        get() = { inflater, parent -> SubscriptionItemBinding.inflate(inflater, parent, false) }

    override val onViewHolderBindCallback: (SubscriptionItemBinding, SubscriptionEntity) -> Unit
        get() = { binding, item ->
            binding.nameText.text = item.title

            val amount = item.amount * if (item.type == SubscriptionTypes.DEBIT.ordinal) -1 else 1
            binding.amountText.text = amount.formatWithCurrency(sign = true)
            binding.amountText.setTextColor(amount.balanceColor(binding.root.context))

            binding.actionInfoText.text = item.day.getNextDate().formatDate(showTime = false)
        }

    override val itemsComparator: (SubscriptionEntity, SubscriptionEntity) -> Boolean
        get() = { first, second ->
            first.title == second.title &&
                    first.amount == second.amount &&
                    first.type == second.type &&
                    first.day == second.day
        }

    override val emptySourceView: EmptySourceViewBinding
        get() = binding.emptySourceView

    override val sourceView: SourceViewBinding
        get() = binding.sourceView

    override val divider: RecyclerView.ItemDecoration
        get() = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )

    override val itemTransitionConfig: ItemTransitionConfig<SubscriptionItemBinding, SubscriptionEntity>
        get() = ItemTransitionConfig(
            directionProducer = { SubscriptionsFragmentDirections.actionCreateSubscription(it) },
            itemIdProducer = { it.subscriptionId.toString() }
        )

    override fun createViewModel(): SubscriptionsViewModel =
        SubscriptionsViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubscriptionsBinding {
        return FragmentSubscriptionsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

        viewModel.updateItems()
    }

    private fun setupViews() {
        binding.newSubscriptionButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    listOf(button shared "item_details_transition"),
                    SubscriptionsFragmentDirections.actionCreateSubscription()
                )
            }
        }
    }
}