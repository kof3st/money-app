package me.kofesst.android.moneyapp.view.asset

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
import me.kofesst.android.moneyapp.databinding.FragmentAssetsBinding
import me.kofesst.android.moneyapp.model.AssetWithSubscriptions
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.view.ExitSharedTransition
import me.kofesst.android.moneyapp.view.FragmentBase
import me.kofesst.android.moneyapp.view.Postpone
import me.kofesst.android.moneyapp.view.navigateToShared
import me.kofesst.android.moneyapp.view.recyclerview.AssetsAdapter
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.ViewModelFactory
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModel

class AssetsFragment : FragmentBase<FragmentAssetsBinding>(), Postpone, ExitSharedTransition {
    private val viewModel: AssetsViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { ViewModelFactory { AssetsViewModel(requireActivity().application) } }
    )

    private lateinit var assetsAdapter: AssetsAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAssetsBinding {
        return FragmentAssetsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObserves()

        viewModel.updateAssets()
    }

    private fun setupViews() {
        assetsAdapter = AssetsAdapter(requireContext()).apply {
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

        binding.assetsView.apply {
            adapter = assetsAdapter
            addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }

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

    private fun setupObserves() {
        lifecycleScope.launchWhenStarted {
            viewModel.assets
                .onEach { assets ->
                    assetsAdapter.submitList(assets)
                    updateTopBarBalance()
                }
                .collect()
        }
    }

    private fun updateTopBarBalance() {
        val balance = viewModel.getTotalBalance()
        binding.topBar.subtitle = balance.formatWithCurrency()
    }
}