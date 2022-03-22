package me.kofesst.android.moneyapp.viewmodel.history

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase

class HistoryViewModel(
    application: Application,
    private val dataStore: DataStore<Preferences>
) : ViewModelBase(application) {
    companion object {
        const val DEFAULT_HISTORY_LIMIT = 5
        private val HISTORY_LIMIT_KEY = intPreferencesKey("history_limit")
    }

    private val _history = MutableStateFlow(listOf<TransactionEntity>())
    val history get() = _history.asStateFlow()

    private val _historyLimit = MutableStateFlow(DEFAULT_HISTORY_LIMIT)
    val historyLimit get() = _historyLimit.asStateFlow()

    /**
     * Обновляет список транзакций [history]
     */
    fun updateHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            _history.value = transactionsDao.getTransactions()
        }
    }

    fun setLimit(limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit {
                it[HISTORY_LIMIT_KEY] = limit
            }
        }
    }

    fun updateLimit() {
        viewModelScope.launch(Dispatchers.IO) {
            _historyLimit.value = dataStore.data.map {
                it[HISTORY_LIMIT_KEY] ?: DEFAULT_HISTORY_LIMIT
            }.first()
        }
    }
}