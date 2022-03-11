package me.kofesst.android.moneyapp.view.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentCreateAssetBinding
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.setEnterSharedTransition
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModel
import me.kofesst.android.moneyapp.viewmodel.asset.AssetsViewModelFactory

class CreateAssetFragment : Fragment() {
    private val viewModel: AssetsViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { AssetsViewModelFactory(requireActivity().application) }
    )

    private lateinit var binding: FragmentCreateAssetBinding
    private var editing: AssetEntity? = null
    private var selectedType: AssetTypes? = null

    private val args: CreateAssetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAssetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEnterSharedTransition(R.integer.shared_transition_duration_short)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editing = args.editing

        setupTopBar()
        setupTypes()
        setupViews()
    }

    private fun setupTopBar() {
        binding.topBar.apply {
            if (editing != null) {
                setTitle(R.string.edit_asset)
            }

            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupTypes() {
        val typesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            AssetTypes.values().map { getString(it.titleRes) }
        )
        binding.typeText.setAdapter(typesAdapter)
        binding.typeText.setOnItemClickListener { _, _, position, _ ->
            selectedType = AssetTypes.values()[position]
        }
    }

    private fun setupViews() {
        if (editing != null) {
            binding.nameText.setText(editing!!.name)
            binding.balanceText.setText(editing!!.balance.toString())

            selectedType = AssetTypes.values()[editing!!.type]
            binding.typeText.setText(getString(selectedType!!.titleRes), false)
        }

        binding.saveButton.apply {
            if (editing != null) {
                setText(R.string.edit_asset)
            }

            setOnClickListener {
                val asset = getModelFromFields() ?: return@setOnClickListener

                if (editing != null) {
                    editing!!.name = asset.name
                    editing!!.balance = asset.balance
                    editing!!.type = asset.type
                    viewModel.addAsset(editing!!)
                } else {
                    viewModel.addAsset(asset)
                }

                findNavController().navigateUp()
            }
        }
    }

    private fun getModelFromFields(): AssetEntity? {
        binding.nameTextLayout.error = null
        binding.balanceTextLayout.error = null
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

        val balanceStr = binding.balanceText.text?.toString()
        if (balanceStr == null || balanceStr.trim().isEmpty()) {
            binding.balanceTextLayout.error = getString(R.string.error_required)
            error = true
        }

        if (error) return null

        val balance: Double
        try {
            balance = balanceStr!!.toDouble()
        } catch (exception: Exception) {
            binding.balanceTextLayout.error = getString(R.string.error_incorrect)
            return null
        }

        if (balance > 1_000_000_000) {
            binding.balanceTextLayout.error = getString(R.string.error_big)
            return null
        }

        if (balance < -1_000_000_000) {
            binding.balanceTextLayout.error = getString(R.string.error_small)
            return null
        }

        return AssetEntity(
            name = name!!,
            balance = balance,
            type = selectedType!!.ordinal
        )
    }
}