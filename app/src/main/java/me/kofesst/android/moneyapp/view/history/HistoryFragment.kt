package me.kofesst.android.moneyapp.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.databinding.FragmentHistoryBinding
import me.kofesst.android.moneyapp.view.FragmentBase
import me.kofesst.android.moneyapp.view.recyclerview.HistoryAdapter
import me.kofesst.android.moneyapp.viewmodel.ViewModelFactory
import me.kofesst.android.moneyapp.viewmodel.history.HistoryViewModel

class HistoryFragment : FragmentBase<FragmentHistoryBinding>() {
    private val viewModel: HistoryViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = { ViewModelFactory { HistoryViewModel(requireActivity().application) } }
    )

    private lateinit var historyAdapter: HistoryAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        setupObserves()
    }

    private fun setupViews() {
        historyAdapter = HistoryAdapter(requireContext())
        binding.historyView.apply {
            adapter = historyAdapter
        }
    }

    private fun setupObserves() {
        lifecycleScope.launch {
            viewModel.history.collectLatest { history ->
                historyAdapter.submitData(history)
            }
        }
    }
}