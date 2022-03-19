package me.kofesst.android.moneyapp.view.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCreateSubscriptionBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.model.default.SubscriptionTypes
import me.kofesst.android.moneyapp.util.showDeleteDialogWithSnackbar
import me.kofesst.android.moneyapp.view.EnterSharedTransition
import me.kofesst.android.moneyapp.view.FragmentBase
import me.kofesst.android.moneyapp.view.navigateUp
import me.kofesst.android.moneyapp.viewmodel.subscription.SubscriptionsViewModel

class CreateSubscriptionFragment :
    FragmentBase<FragmentCreateSubscriptionBinding, SubscriptionsViewModel>(
        SubscriptionsViewModel::class
    ), EnterSharedTransition {
    private var selectedAsset: AssetEntity? = null
    private var selectedType: SubscriptionTypes? = null

    private val args: CreateSubscriptionFragmentArgs by navArgs()
    private val editing: SubscriptionEntity? by lazy { args.editing }

    override fun createViewModel(): SubscriptionsViewModel =
        SubscriptionsViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateSubscriptionBinding {
        return FragmentCreateSubscriptionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopBar()
        setupTypes()
        setupViews()
    }

    private fun setupTopBar() {
        binding.topBar.apply {
            if (editing != null) setTitle(R.string.edit_subscription)
            else menu.findItem(R.id.delete).isVisible = false

            setNavigationOnClickListener { navigateUp() }

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> {
                        showDeleteDialogWithSnackbar(
                            fragmentView = requireActivity().findViewById(R.id.fragment_container),
                            dialogTitleRes = R.string.confirm_dialog_title,
                            dialogMessageRes = R.string.delete_subscription_message,
                            snackbarMessageRes = R.string.snackbar_subscription_deleted,
                            deleteAction = {
                                viewModel.deleteSubscription(editing!!)
                                navigateUp()
                            },
                            undoAction = {
                                viewModel.addSubscription(editing!!)
                            }
                        )
                    }
                }

                true
            }
        }
    }

    private fun setupTypes() {
        val typesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            SubscriptionTypes.values().map { getString(it.titleRes) }
        )
        binding.typeText.setAdapter(typesAdapter)
        binding.typeText.setOnItemClickListener { _, _, position, _ ->
            selectedType = SubscriptionTypes.values()[position]
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val assets = viewModel.getAssets()
            val assetsAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                assets
            )

            withContext(Dispatchers.Main) {
                binding.assetText.setAdapter(assetsAdapter)
                binding.assetText.setOnItemClickListener { _, _, position, _ ->
                    selectedAsset = assets[position]
                }
            }
        }
    }

    private fun setupViews() {
        editing?.let {
            selectedType = SubscriptionTypes.values()[it.type]
            binding.saveButton.setText(R.string.edit_subscription)
            binding.nameText.setText(it.title)
            binding.amountText.setText(it.amount.toString())
            binding.dayText.setText(it.day.toString())
            binding.typeText.setText(getString(SubscriptionTypes.values()[it.type].titleRes), false)

            lifecycleScope.launch(Dispatchers.IO) {
                val asset = viewModel.getAsset(it.assetId) ?: return@launch
                selectedAsset = asset

                withContext(Dispatchers.Main) {
                    binding.assetText.setText(asset.toString(), false)
                }
            }
        }

        binding.saveButton.apply {
            setOnClickListener {
                val subscription = getModelFromFields() ?: return@setOnClickListener

                if (editing != null) {
                    editing!!.apply {
                        title = subscription.title
                        amount = subscription.amount
                        day = subscription.day
                        type = subscription.type
                    }
                    viewModel.addSubscription(editing!!)
                } else viewModel.addSubscription(subscription)

                navigateUp()
            }
        }
    }

    private fun getModelFromFields(): SubscriptionEntity? {
        binding.nameTextLayout.error = null
        binding.amountTextLayout.error = null
        binding.assetTextLayout.error = null
        binding.dayTextLayout.error = null
        binding.typeTextLayout.error = null

        var error = false

        val name = binding.nameText.text?.toString()
        if (name == null || name.trim().isEmpty()) {
            binding.nameTextLayout.error = getString(R.string.error_required)
            error = true
        } else if (name.length > binding.nameTextLayout.counterMaxLength) {
            binding.nameTextLayout.error = getString(R.string.error_counter)
            error = true
        }

        if (selectedType == null) {
            binding.typeTextLayout.error = getString(R.string.error_required)
            error = true
        }

        if (selectedAsset == null) {
            binding.assetTextLayout.error = getString(R.string.error_required)
            error = true
        }

        val amountStr = binding.amountText.text?.toString()
        if (amountStr == null || amountStr.trim().isEmpty()) {
            binding.amountTextLayout.error = getString(R.string.error_required)
            error = true
        }

        val dayStr = binding.dayText.text?.toString()
        if (dayStr == null || dayStr.trim().isEmpty()) {
            binding.dayTextLayout.error = getString(R.string.error_required)
            error = true
        }

        if (error) return null

        val amount: Double
        try {
            amount = amountStr!!.toDouble()
        } catch (exception: Exception) {
            binding.amountTextLayout.error = getString(R.string.error_incorrect)
            return null
        }

        if (amount > 1_000_000_000) {
            binding.amountTextLayout.error = getString(R.string.error_big)
            return null
        }

        val day: Int
        try {
            day = dayStr!!.toInt()
        } catch (exception: Exception) {
            binding.dayTextLayout.error = getString(R.string.error_incorrect)
            return null
        }

        if (day > 28 || day < 1) {
            binding.dayTextLayout.error = getString(R.string.error_subscription_day)
            return null
        }

        return SubscriptionEntity(
            title = name!!,
            amount = amount,
            day = day,
            type = selectedType!!.ordinal,
            assetId = selectedAsset!!.assetId
        )
    }
}