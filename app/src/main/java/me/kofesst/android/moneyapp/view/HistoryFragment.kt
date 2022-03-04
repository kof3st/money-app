package me.kofesst.android.moneyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentHistoryBinding
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.util.CasesUtil
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.view.recyclerview.HistoryAdapter
import me.kofesst.android.moneyapp.viewmodel.HistoryViewModel
import me.kofesst.android.moneyapp.viewmodel.factory.HistoryViewModelFactory

class HistoryFragment : Fragment() {
    companion object {
        private const val TRANSACTIONS_COUNT_CASES_WORD_UID = "transactions"
    }

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
        setupCases()
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

    private fun setupCases() {
        CasesUtil.registerWord(
            uid = TRANSACTIONS_COUNT_CASES_WORD_UID,
            firstCase = "транзакция",
            secondCase = "транзакции",
            thirdCase = "транзакций"
        )
    }

    private fun setupObserves() {
        viewModel.historyLiveData.observe(viewLifecycleOwner) {
            historyAdapter.submitList(it)
            updateTopBar(it.size)
            updateInfo(it.filter { item -> item.categoryId != null })
        }
    }

    private fun updateTopBar(count: Int) {
        binding.topBar.subtitle = getString(R.string.count_format)
            .format(CasesUtil.getCase(TRANSACTIONS_COUNT_CASES_WORD_UID, count))
    }

    private fun updateInfo(list: List<TransactionEntity>) {
        binding.incomeText.text = list.filter { it.amount > 0.0 }
            .sumOf { it.amount }
            .formatWithCurrency(sign = true)

        binding.lossText.text = list.filter { it.amount < 0.0 }
            .sumOf { it.amount }
            .formatWithCurrency(sign = true)
    }
}