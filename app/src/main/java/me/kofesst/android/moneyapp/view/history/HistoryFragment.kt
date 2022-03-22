package me.kofesst.android.moneyapp.view.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.EmptySourceViewBinding
import me.kofesst.android.moneyapp.databinding.FragmentHistoryBinding
import me.kofesst.android.moneyapp.databinding.HistoryItemBinding
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.util.CasesUtil
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatDate
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.view.ListFragmentBase
import me.kofesst.android.moneyapp.viewmodel.history.HistoryViewModel

class HistoryFragment :
    ListFragmentBase<FragmentHistoryBinding, HistoryViewModel, TransactionEntity, HistoryItemBinding>(
        HistoryViewModel::class
    ) {
    companion object {
        private const val HISTORY_LIMIT_CASES_WORD_UID = "history_limit"
        private const val HISTORY_LIMIT_MINIMUM = 5
        private const val HISTORY_LIMIT_MAXIMUM = 1000
    }

    override val viewHolderBindingProducer: (LayoutInflater, ViewGroup) -> HistoryItemBinding
        get() = { inflater, parent -> HistoryItemBinding.inflate(inflater, parent, false) }

    override val onViewHolderBindCallback: (HistoryItemBinding, TransactionEntity) -> Unit
        get() = { binding, item ->
            binding.titleText.text = item.title

            binding.amountText.text = item.amount.formatWithCurrency(sign = true)
            binding.amountText.setTextColor(item.amount.balanceColor(binding.root.context))

            binding.assetNameText.text = item.assetName
            binding.categoryText.text = item.categoryName
            binding.dateText.text = item.date.formatDate()

            if (item.targetName == null) {
                binding.targetRow.visibility = View.GONE
            } else {
                binding.amountText.text = (-item.amount).formatWithCurrency(sign = true)
                binding.amountText.setTextColor((-item.amount).balanceColor(binding.root.context))

                binding.targetNameText.text = item.targetName
                binding.targetAmountText.text = item.amount.formatWithCurrency(sign = true)
                binding.targetAmountText.setTextColor(item.amount.balanceColor(binding.root.context))
            }

        }

    override val itemsComparator: (TransactionEntity, TransactionEntity) -> Boolean
        get() = { first, second -> first.transactionId == second.transactionId }

    override val listStateFlow: StateFlow<List<TransactionEntity>>
        get() = viewModel.history

    override val emptySourceView: EmptySourceViewBinding
        get() = binding.emptySourceView

    override val divider: RecyclerView.ItemDecoration
        get() = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )

    private val Context.dataStore by preferencesDataStore("user_prefs")

    override fun createViewModel(): HistoryViewModel =
        HistoryViewModel(requireActivity().application, requireContext().dataStore)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(inflater, container, false)
    }

    override fun getRecyclerView(): RecyclerView = binding.historyView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObserves()
        setupCases()

        viewModel.updateHistory()
        viewModel.updateLimit()
    }

    private fun setupViews() {
        binding.limitSaveButton.apply {
            setOnClickListener {
                try {
                    val limitInput = binding.limitText.text.toString()
                    val limit = limitInput.toInt()

                    if (limit < HISTORY_LIMIT_MINIMUM) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.small_limit).format(
                                CasesUtil.getCase(
                                    uid = HISTORY_LIMIT_CASES_WORD_UID,
                                    amount = HISTORY_LIMIT_MINIMUM
                                )
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    if (limit > HISTORY_LIMIT_MAXIMUM) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.big_limit).format(
                                CasesUtil.getCase(
                                    uid = HISTORY_LIMIT_CASES_WORD_UID,
                                    amount = HISTORY_LIMIT_MAXIMUM
                                )
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    viewModel.setLimit(limit)

                    Toast.makeText(
                        requireContext(),
                        R.string.limit_saved,
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (exception: NumberFormatException) {
                    Toast.makeText(
                        requireContext(),
                        R.string.error_incorrect,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupObserves() {
        lifecycleScope.launchWhenStarted {
            viewModel.historyLimit.onEach { limit ->
                binding.limitText.setText(limit.toString())

                binding.limitCase.text = CasesUtil.getCase(
                    uid = HISTORY_LIMIT_CASES_WORD_UID,
                    amount = limit,
                    includeAmount = false
                )
            }.collect()
        }
    }

    private fun setupCases() {
        CasesUtil.registerWord(
            uid = HISTORY_LIMIT_CASES_WORD_UID,
            firstCase = "запись",
            secondCase = "записи",
            thirdCase = "записей"
        )
    }
}