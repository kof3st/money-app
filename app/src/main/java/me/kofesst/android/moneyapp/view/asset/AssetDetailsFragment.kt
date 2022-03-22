package me.kofesst.android.moneyapp.view.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentAssetDetailsBinding
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.util.shared
import me.kofesst.android.moneyapp.util.showDeleteDialogWithSnackbar
import me.kofesst.android.moneyapp.view.*
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModel

class AssetDetailsFragment :
    ItemDetailsFragmentBase<FragmentAssetDetailsBinding, AssetsViewModel>(
        AssetsViewModel::class
    ), EnterSharedTransition, ExitSharedTransition {
    override val topBar: MaterialToolbar
        get() = binding.topBar

    override val topBarConfig: FragmentTopBarConfig
        get() = FragmentTopBarConfig(
            titleSetter = { it.title = targetAsset.asset.name },
            hasBackButton = true
        )

    override val itemId: String
        get() = targetAsset.asset.assetId.toString()

    private val args: AssetDetailsFragmentArgs by navArgs()
    private val targetAsset by lazy { args.targetAsset }

    override fun createViewModel(): AssetsViewModel =
        AssetsViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAssetDetailsBinding {
        return FragmentAssetDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
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

    private fun setupActions() {
        binding.editButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    listOf(button shared R.string.edit_shared_transition_name),
                    AssetDetailsFragmentDirections.actionEditAsset(targetAsset.asset)
                )
            }
        }

        binding.transactionButton.apply {
            setOnClickListener { button ->
                navigateToShared(
                    listOf(button shared R.string.add_transaction_transition_name),
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
                    listOf(button shared R.string.add_transaction_transition_name),
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