package me.kofesst.android.moneyapp.viewmodel.history

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.model.history.HistoryFilter
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase

class HistoryViewModel(application: Application) : ViewModelBase(application) {
    private val _currentFilter: MutableStateFlow<HistoryFilter> =
        MutableStateFlow(HistoryFilter.DayHistoryFilter)
    val currentFilter get() = _currentFilter.asStateFlow()

    private val _history = MutableStateFlow(listOf<TransactionEntity>())
    val history get() = _history.asStateFlow()

    private val _filteredHistory = MutableStateFlow(listOf<TransactionEntity>())
    val filteredHistory get() = _filteredHistory.asStateFlow()

    fun filterHistory(filter: HistoryFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentFilter.value = filter
            _filteredHistory.value = history.value.filter { filter.filter(it) }
        }
    }

    /**
     * Обновляет список транзакций [history]
     */
    fun updateHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            _history.value = transactionsDao.getTransactions()
            filterHistory(currentFilter.value)
        }
    }
}