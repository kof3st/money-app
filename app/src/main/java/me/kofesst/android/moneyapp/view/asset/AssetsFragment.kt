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
import me.kofesst.android.moneyapp.databinding.FragmentAssetsBinding
import me.kofesst.android.moneyapp.model.AssetWithSubscriptions
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.view.ExitSharedTransition
import me.kofesst.android.moneyapp.view.ListFragmentBase
import me.kofesst.android.moneyapp.view.Postpone
import me.kofesst.android.moneyapp.view.navigateToShared
import me.kofesst.android.moneyapp.view.recyclerview.AssetViewHolder
import me.kofesst.android.moneyapp.view.recyclerview.AssetsAdapter
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModel

class AssetsFragment : ListFragmentBase<FragmentAssetsBinding,
        AssetsViewModel,
        AssetWithSubscriptions,
        AssetViewHolder,
        AssetsAdapter>(
    AssetsViewModel::class
), Postpone, ExitSharedTransition {
    override val divider: RecyclerView.ItemDecoration
        get() = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )

    override val listStateFlow: StateFlow<List<AssetWithSubscriptions>>
        get() = viewModel.assets

    override fun createViewModel(): AssetsViewModel =
        AssetsViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAssetsBinding {
        return FragmentAssetsBinding.inflate(inflater, container, false)
    }

    override fun createAdapter() = AssetsAdapter(requireContext()).apply {
        itemClickListener = object : ItemClickListener<AssetWithSubscriptions> {
            override fun onClick(view: View, item: AssetWithSubscriptions) {
                navigateToShared(
                    R.string.asset_details_transition_name,
                    binding.topBar,
                    AssetsFragmentDirections.actionAssetDetails(item)
                )
            }

            override fun onLongClick(view: View, item: AssetWithSubscriptions) {}
        }
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