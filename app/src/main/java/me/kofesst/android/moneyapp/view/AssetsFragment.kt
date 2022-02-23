package me.kofesst.android.moneyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.kofesst.android.moneyapp.databinding.FragmentAssetsBinding
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.view.dialog.AssetModelDialog
import me.kofesst.android.moneyapp.view.recyclerview.AssetsAdapter
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
        setupViewModel()
        setupViews()
        setupObserves()

        viewModel.updateAssets()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            AssetsViewModelFactory(requireActivity().application)
        )[AssetsViewModel::class.java]
    }

    private fun setupViews() {
        assetsAdapter = AssetsAdapter(requireContext())
        binding.assetsView.apply {
            adapter = assetsAdapter
        }

        binding.newAssetButton.apply {
            setOnClickListener {
                val dialog = AssetModelDialog({ viewModel.addAsset(it) })
                dialog.show(parentFragmentManager, "create_asset_dialog")
            }
        }
    }

    private fun setupObserves() {
        viewModel.assetsLiveData.observe(viewLifecycleOwner) {
            assetsAdapter.submitList(it.toMutableList())
            updateTopBarBalance()
        }
    }

    private fun updateTopBarBalance() {
        val balance = viewModel.getTotalBalance()
        binding.topBar.subtitle = balance.formatWithCurrency()
        binding.topBar.setSubtitleTextColor(balance.balanceColor(requireContext()))
    }
}