package me.kofesst.android.moneyapp.view.asset

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
import me.kofesst.android.moneyapp.databinding.FragmentAssetsBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.util.include
import me.kofesst.android.moneyapp.util.setPostpone
import me.kofesst.android.moneyapp.util.setExitSharedTransition
import me.kofesst.android.moneyapp.view.recyclerview.AssetsAdapter
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModel
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModelFactory

class AssetsFragment: Fragment() {
    private val viewModel: AssetsViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { AssetsViewModelFactory(requireActivity().application) }
    )

    private lateinit var binding: FragmentAssetsBinding
    private lateinit var assetsAdapter: AssetsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssetsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPostpone(view)
        setExitSharedTransition(R.integer.shared_transition_duration_short)

        setupViews()
        setupObserves()

        viewModel.updateAssets()
    }

    private fun setupViews() {
        assetsAdapter = AssetsAdapter(requireContext()).apply {
            itemClickListener = object : ItemClickListener<AssetEntity> {
                override fun onClick(view: View, item: AssetEntity) {
                    val extras = R.string.asset_details_transition_name include binding.topBar
                    val direction = AssetsFragmentDirections.actionAssetDetails(item)
                    findNavController().navigate(direction, extras)
                }

                override fun onLongClick(view: View, item: AssetEntity) {}
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
                val extras = R.string.edit_shared_transition_name include button
                val direction = AssetsFragmentDirections.actionCreateAsset()
                findNavController().navigate(direction, extras)
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