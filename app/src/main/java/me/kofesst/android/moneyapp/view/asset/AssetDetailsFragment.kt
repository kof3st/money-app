package me.kofesst.android.moneyapp.view.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentAssetDetailsBinding
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.util.showDeleteDialogWithSnackbar
import me.kofesst.android.moneyapp.view.*
import me.kofesst.android.moneyapp.viewmodel.ViewModelFactory
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModel

class AssetDetailsFragment : FragmentBase<FragmentAssetDetailsBinding>(), EnterSharedTransition,
    ExitSharedTransition {
    private val viewModel: AssetsViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { ViewModelFactory { AssetsViewModel(requireActivity().application) } }
    )

    private val args: AssetDetailsFragmentArgs by navArgs()
    private val targetAsset by lazy { args.targetAsset }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAssetDetailsBinding {
        return FragmentAssetDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupTopBar()
        setupActions()
    }

    private fun setupViews() {
        binding.balanceText.apply {
            text = targetAsset.asset.balance.formatWithCurrency()
            setTextColor(targetAsset.asset.balance.balanceColor(requireContext()))
        }

        binding.typeText.apply {
            setText(AssetTypes.values()[targetAsset.asset.type].titleRes)
        }
    }

    private fun setupTopBar() {
        binding.topBar.apply {
            title = targetAsset.asset.name
            setNavigationOnClickListener {
                navigateUp()
            }
        }
    }

    private fun setupActions() {
        binding.editButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    R.string.edit_shared_transition_name,
                    button,
                    AssetDetailsFragmentDirections.actionEditAsset(targetAsset.asset)
                )
            }
        }

        binding.transactionButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    R.string.add_transaction_transition_name,
                    button,
                    AssetDetailsFragmentDirections.actionCreateTransaction(
                        targetAsset = targetAsset.asset,
                        isTransfer = false
                    )
                )
            }
        }

        binding.transferButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    R.string.add_transaction_transition_name,
                    button,
                    AssetDetailsFragmentDirections.actionCreateTransaction(
                        targetAsset = targetAsset.asset,
                        isTransfer = true
                    )
                )
            }
        }

        binding.deleteButton.apply {
            setOnClickListener {
                showDeleteDialogWithSnackbar(
                    fragmentView = requireActivity().findViewById(R.id.fragment_container),
                    dialogTitleRes = R.string.confirm_dialog_title,
                    dialogMessageRes = R.string.delete_asset_message,
                    snackbarMessageRes = R.string.snackbar_asset_deleted,
                    deleteAction = {
                        viewModel.deleteAsset(targetAsset.asset)
                        navigateUp()
                    },
                    undoAction = {
                        viewModel.addAsset(targetAsset.asset)

                        // Restoring deleted subscriptions
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.addSubscriptions(targetAsset.subscriptions)
                        }
                    }
                )
            }
        }
    }
}