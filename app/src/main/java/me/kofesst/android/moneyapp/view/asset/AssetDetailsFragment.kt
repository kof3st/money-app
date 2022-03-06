package me.kofesst.android.moneyapp.view.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentAssetDetailsBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.*
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModel
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModelFactory

class AssetDetailsFragment: Fragment() {
    private val viewModel: AssetsViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { AssetsViewModelFactory(requireActivity().application) }
    )

    private lateinit var binding: FragmentAssetDetailsBinding
    private lateinit var targetAsset: AssetEntity

    private val args: AssetDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssetDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setEnterSharedTransition(R.integer.shared_transition_duration_short)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        targetAsset = args.targetAsset
        setExitSharedTransition(R.integer.shared_transition_duration_short)

        setupViews()
        setupTopBar()
        setupActions()
    }

    private fun setupViews() {
        binding.balanceText.apply {
            text = targetAsset.balance.formatWithCurrency()
            setTextColor(targetAsset.balance.balanceColor(requireContext()))
        }

        binding.typeText.apply {
            setText(AssetTypes.values()[targetAsset.type].titleRes)
        }
    }

    private fun setupTopBar() {
        binding.topBar.apply {
            title = targetAsset.name
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupActions() {
        binding.transactionButton.apply {
            setOnClickListener { button ->
                val extras = R.string.add_transaction_transition_name include button
                val direction = AssetDetailsFragmentDirections.actionAssetDetailsFragmentToCreateTransactionFragment(
                    targetAsset = targetAsset,
                    isTransfer = false
                )
                findNavController().navigate(direction, extras)
            }
        }

        binding.transferButton.apply {
            setOnClickListener { button ->
                val extras = R.string.transfer_transition_name include button
                val direction = AssetDetailsFragmentDirections.actionAssetDetailsFragmentToCreateTransactionFragment(
                    targetAsset = targetAsset,
                    isTransfer = true
                )
                findNavController().navigate(direction, extras)
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
                        viewModel.deleteAsset(targetAsset)
                        findNavController().navigateUp()
                    },
                    undoAction = {
                        viewModel.addAsset(targetAsset)
                    }
                )
            }
        }
    }
}