package me.kofesst.android.moneyapp.view.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentSubscriptionsBinding
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.util.include
import me.kofesst.android.moneyapp.util.setExitSharedTransition
import me.kofesst.android.moneyapp.util.setPostpone
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.view.recyclerview.SubscriptionsAdapter
import me.kofesst.android.moneyapp.viewmodel.asset.SubscriptionsViewModel
import me.kofesst.android.moneyapp.viewmodel.asset.SubscriptionsViewModelFactory

class SubscriptionsFragment : Fragment() {
    private val viewModel: SubscriptionsViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { SubscriptionsViewModelFactory(requireActivity().application) }
    )

    private lateinit var binding: FragmentSubscriptionsBinding
    private lateinit var subscriptionsAdapter: SubscriptionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPostpone(view)
        setExitSharedTransition(R.integer.shared_transition_duration_short)

        setupViews()
        setupObserves()

        viewModel.updateSubscriptions()
    }

    private fun setupViews() {
        subscriptionsAdapter = SubscriptionsAdapter(requireContext()).apply {
            itemClickListener = object : ItemClickListener<SubscriptionEntity> {
                override fun onClick(view: View, item: SubscriptionEntity) {
                    val extras = R.string.edit_shared_transition_name include binding.newSubscriptionButton
                    val direction = SubscriptionsFragmentDirections.actionCreateSubscription(
                        editing = item
                    )
                    findNavController().navigate(direction, extras)
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
                val extras = R.string.edit_shared_transition_name include button
                val direction = SubscriptionsFragmentDirections.actionCreateSubscription()
                findNavController().navigate(direction, extras)
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