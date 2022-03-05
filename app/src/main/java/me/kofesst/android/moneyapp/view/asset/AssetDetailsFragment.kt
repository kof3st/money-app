package me.kofesst.android.moneyapp.view.asset

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialElevationScale
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentAssetDetailsBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.util.showDeleteDialogWithSnackbar
import me.kofesst.android.moneyapp.viewmodel.AssetsViewModel
import me.kofesst.android.moneyapp.viewmodel.factory.AssetsViewModelFactory

class AssetDetailsFragment: Fragment() {
    private lateinit var binding: FragmentAssetDetailsBinding
    private lateinit var viewModel: AssetsViewModel
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

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment_container
            duration = resources.getInteger(R.integer.shared_transition_duration_short)
                .toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        targetAsset = args.targetAsset

        setupViewModel()
        setupViews()
        setupTopBar()
        setupActions()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(),
            AssetsViewModelFactory(requireActivity().application)
        )[AssetsViewModel::class.java]
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
            setOnClickListener {
                exitTransition = MaterialElevationScale(false).apply {
                    duration =
                        resources.getInteger(R.integer.shared_transition_duration_short).toLong()
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration =
                        resources.getInteger(R.integer.shared_transition_duration_short).toLong()
                }

                val transitionName = getString(R.string.add_transaction_transition_name)
                val extras = FragmentNavigatorExtras(it to transitionName)
                val direction = AssetDetailsFragmentDirections.actionAssetDetailsFragmentToCreateTransactionFragment(
                    targetAsset = targetAsset,
                    isTransfer = false
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