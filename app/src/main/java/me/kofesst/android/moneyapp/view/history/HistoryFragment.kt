package me.kofesst.android.moneyapp.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout
import com.robinhood.ticker.TickerUtils
import kotlinx.coroutines.flow.StateFlow
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.databinding.FragmentHistoryBinding
import me.kofesst.android.moneyapp.databinding.HistoryItemBinding
import me.kofesst.android.moneyapp.databinding.SourceViewBinding
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.model.history.HistoryFilter
import me.kofesst.android.moneyapp.util.balanceColor
import me.kofesst.android.moneyapp.util.formatDate
import me.kofesst.android.moneyapp.util.formatWithCurrency
import me.kofesst.android.moneyapp.view.ListFragmentBase
import me.kofesst.android.moneyapp.view.observe
import me.kofesst.android.moneyapp.viewmodel.history.HistoryViewModel

class HistoryFragment :
    ListFragmentBase<FragmentHistoryBinding, HistoryViewModel, TransactionEntity, HistoryItemBinding>(
        HistoryViewModel::class
    ) {
    override val itemLayoutResId: Int
        get() = R.layout.history_item

    override val viewHolderBindingProducer: (View) -> HistoryItemBinding
        get() = { HistoryItemBinding.bind(it) }

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

    override val sourceView: SourceViewBinding
        get() = binding.sourceView

    override val sourceStateFlow: StateFlow<List<TransactionEntity>>
        get() = viewModel.filteredHistory

    override fun createViewModel(): HistoryViewModel =
        HistoryViewModel(requireActivity().application)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObserves()

        viewModel.updateItems()
    }

    private fun setupViews() {
        binding.creditsInfo.apply {
            setCharacterLists(TickerUtils.provideNumberList())
            typeface = ResourcesCompat.getFont(requireContext(), R.font.rubikmedium)
        }
        binding.debitsInfo.apply {
            setCharacterLists(TickerUtils.provideNumberList())
            typeface = ResourcesCompat.getFont(requireContext(), R.font.rubikmedium)
        }

        binding.historyFilters.apply {
            HistoryFilter.FILTERS.forEach { filter ->
                addTab(newTab().setText(filter.titleResId))
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab == null) return

                    val historyTab = HistoryFilter.FILTERS[tab.position]
                    viewModel.filterHistory(historyTab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun setupObserves() {
        observe(viewModel.currentFilter) { filter ->
            binding.historyFilters.getTabAt(filter.id)?.select()
        }

        observe(viewModel.filteredHistory) { list ->
            val history = list.filter { it.targetId == null }
            val credits = history.filter { it.amount > 0.0 }.sumOf { it.amount }
            val debits = history.filter { it.amount < 0.0 }.sumOf { it.amount }

            binding.creditsInfo.setText(credits.formatWithCurrency(sign = true), true)
            binding.debitsInfo.setText(debits.formatWithCurrency(sign = true), true)
        }
    }
}