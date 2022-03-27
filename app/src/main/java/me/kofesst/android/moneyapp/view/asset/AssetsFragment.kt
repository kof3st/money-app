package me.kofesst.android.moneyapp.view.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.AssetItemBinding
import me.kofesst.android.moneyapp.databinding.EmptySourceViewBinding
import me.kofesst.android.moneyapp.databinding.FragmentAssetsBinding
import me.kofesst.android.moneyapp.databinding.SourceViewBinding
import me.kofesst.android.moneyapp.model.AssetWithSubscriptions
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.util.shared
import me.kofesst.android.moneyapp.view.*
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModel

class AssetsFragment :
    ListFragmentBase<FragmentAssetsBinding, AssetsViewModel, AssetWithSubscriptions, AssetItemBinding>(
        AssetsViewModel::class
    ), Postpone, ExitSharedTransition {
    override val viewHolderBindingProducer: (LayoutInflater, ViewGroup) -> AssetItemBinding
        get() = { inflater, parent -> AssetItemBinding.inflate(inflater, parent, false) }

    override val onViewHolderBindCallback: (AssetItemBinding, AssetWithSubscriptions) -> Unit
        get() = { binding, item ->
            val itemType = AssetTypes.values()[item.asset.type]
            binding.typeIcon.setImageResource(itemType.iconRes)

            binding.nameText.text = item.asset.name
            binding.balanceText.text = item.asset.balance.formatWithCurrency()
            binding.balanceText.setTextColor(item.asset.balance.balanceColor(binding.root.context))
        }

    override val itemsComparator: (AssetWithSubscriptions, AssetWithSubscriptions) -> Boolean
        get() = { first, second -> first.asset.assetId == second.asset.assetId }

    override val emptySourceView: EmptySourceViewBinding
        get() = binding.emptySourceView

    override val sourceView: SourceViewBinding
        get() = binding.sourceView

    override val itemTransitionConfig: ItemTransitionConfig<AssetItemBinding, AssetWithSubscriptions>
        get() = ItemTransitionConfig(
            directionProducer = { AssetsFragmentDirections.actionAssetDetails(it) },
            itemIdProducer = { it.asset.assetId.toString() }
        )

    override fun createViewModel(): AssetsViewModel =
        AssetsViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAssetsBinding {
        return FragmentAssetsBinding.inflate(inflater, container, false)
    }

    override fun onListObserved(list: List<AssetWithSubscriptions>) = updateTopBarBalance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

        viewModel.updateItems()
    }

    private fun setupViews() {
        binding.newAssetButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    listOf(button shared R.string.edit_shared_transition_name),
                    AssetsFragmentDirections.actionCreateAsset()
                )
            }
        }
    }

    private fun updateTopBarBalance() {
        val balance = viewModel.getTotalBalance()
        binding.topBar.subtitle = balance.formatWithCurrency()
    }
}