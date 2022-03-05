package me.kofesst.android.moneyapp.view.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.transition.platform.MaterialElevationScale
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentAssetsBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.view.recyclerview.AssetsAdapter
import me.kofesst.android.moneyapp.view.recyclerview.ItemClickListener
import me.kofesst.android.moneyapp.viewmodel.AssetsViewModel
import me.kofesst.android.moneyapp.viewmodel.factory.AssetsViewModelFactory

class AssetsFragment : Fragment() {
    private lateinit var binding: FragmentAssetsBinding
    private lateinit var viewModel: AssetsViewModel
    private lateinit var assetsAdapter: AssetsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssetsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        setupViewModel()
        setupViews()
        setupObserves()

        viewModel.updateAssets()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(),
            AssetsViewModelFactory(requireActivity().application)
        )[AssetsViewModel::class.java]
    }

    private fun setupViews() {
        assetsAdapter = AssetsAdapter(requireContext()).apply {
            itemClickListener = object : ItemClickListener<AssetEntity> {
                override fun onClick(view: View, item: AssetEntity) {
                    exitTransition = MaterialElevationScale(false).apply {
                        duration =
                            resources.getInteger(R.integer.shared_transition_duration_short).toLong()
                    }

                    reenterTransition = MaterialElevationScale(true).apply {
                        duration =
                            resources.getInteger(R.integer.shared_transition_duration_short).toLong()
                    }

                    val transitionName = getString(R.string.asset_details_transition_name)
                    val extras = FragmentNavigatorExtras(binding.topBar to transitionName)
                    val direction = AssetsFragmentDirections.actionAssetsFragmentToAssetDetailsFragment(item)
                    findNavController().navigate(direction, extras)
                }

                override fun onLongClick(view: View, item: AssetEntity) {}
            }
        }

        binding.assetsView.apply {
            adapter = assetsAdapter
            addItemDecoration(MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            ))
        }

        binding.newAssetButton.apply {
            setOnClickListener {
                exitTransition = MaterialElevationScale(false).apply {
                    duration =
                        resources.getInteger(R.integer.shared_transition_duration_short).toLong()
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration =
                        resources.getInteger(R.integer.shared_transition_duration_short).toLong()
                }

                val transitionName = getString(R.string.edit_shared_transition_name)
                val extras = FragmentNavigatorExtras(it to transitionName)
                val direction = AssetsFragmentDirections.actionAssetsFragmentToCreateAssetFragment()
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