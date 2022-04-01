package me.kofesst.android.moneyapp.viewmodel.history

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.model.history.HistoryFilter
import me.kofesst.android.moneyapp.viewmodel.ListViewModelBase

class HistoryViewModel(
    application: Application
) : ListViewModelBase<TransactionEntity>(application) {
    private val _currentFilter: MutableStateFlow<HistoryFilter> =
        MutableStateFlow(HistoryFilter.DayHistoryFilter)
    val currentFilter get() = _currentFilter.asStateFlow()

    private val _filteredHistory = MutableStateFlow(listOf<TransactionEntity>())
    val filteredHistory get() = _filteredHistory.asStateFlow()

    /**
     * Возвращает список транзакций.
     */
    override suspend fun getItems(): List<TransactionEntity> = transactionsDao.getTransactions()

    /**
     * Фильтрует список транзакций по фильтру [filter].
     */
    fun filterHistory(filter: HistoryFilter) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentFilter.value = filter
            _filteredHistory.value = items.value.filter { filter.filter(it) }
        }
    }

    /**
     * Обновляет список транзакций.
     */
    override fun updateItems(callback: () -> Unit) {
        super.updateItems {
            callback()
            filterHistory(currentFilter.value)
        }
    }
}