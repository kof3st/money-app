package me.kofesst.android.moneyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentHistoryBinding
import me.kofesst.android.moneyapp.view.recyclerview.HistoryAdapter
import me.kofesst.android.moneyapp.viewmodel.HistoryViewModel
import me.kofesst.android.moneyapp.viewmodel.factory.HistoryViewModelFactory

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModel()
        setupViews()
        setupObserves()

        viewModel.updateHistory()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this,
            factory = HistoryViewModelFactory(requireActivity().application)
        )[HistoryViewModel::class.java]
    }

    private fun setupViews() {
        historyAdapter = HistoryAdapter(requireContext())

        binding.historyView.apply {
            adapter = historyAdapter
        }
    }

    private fun setupObserves() {
        viewModel.historyLiveData.observe(viewLifecycleOwner) {
            historyAdapter.submitList(it.toList())
        }
    }
}