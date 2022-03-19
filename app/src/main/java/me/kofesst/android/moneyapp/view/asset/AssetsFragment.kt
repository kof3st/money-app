package me.kofesst.android.moneyapp.view.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.StateFlow
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.AssetItemBinding
import me.kofesst.android.moneyapp.databinding.FragmentAssetsBinding
import me.kofesst.android.moneyapp.model.AssetWithSubscriptions
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.view.ExitSharedTransition
import me.kofesst.android.moneyapp.view.ListFragmentBase
import me.kofesst.android.moneyapp.view.Postpone
import me.kofesst.android.moneyapp.view.navigateToShared
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
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

    override val listStateFlow: StateFlow<List<AssetWithSubscriptions>>
        get() = viewModel.assets

    override val divider: RecyclerView.ItemDecoration
        get() = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )

    override val onItemClickListener: ItemClickListener<AssetWithSubscriptions>
        get() = object : ItemClickListener<AssetWithSubscriptions> {
            override fun onClick(view: View, item: AssetWithSubscriptions) {
                navigateToShared(
                    R.string.asset_details_transition_name,
                    binding.topBar,
                    AssetsFragmentDirections.actionAssetDetails(item)
                )
            }

            override fun onLongClick(view: View, item: AssetWithSubscriptions) = Unit
        }

    override fun createViewModel(): AssetsViewModel =
        AssetsViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAssetsBinding {
        return FragmentAssetsBinding.inflate(inflater, container, false)
    }

    override fun getRecyclerView(): RecyclerView = binding.assetsView

    override fun onListObserved(list: List<AssetWithSubscriptions>) = updateTopBarBalance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        viewModel.updateAssets()
    }

    private fun setupViews() {
        binding.newAssetButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    R.string.edit_shared_transition_name,
                    button,
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