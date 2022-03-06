package me.kofesst.android.moneyapp.viewmodel.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.database.MainDatabase
import me.kofesst.android.moneyapp.model.TransactionEntity

class HistoryViewModel(
    application: Application
): AndroidViewModel(application) {
    private val database = MainDatabase.get(application)

    val historyLiveData = MutableLiveData<List<TransactionEntity>>()

    fun updateHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val history = database.getTransactionsDao().getTransactions()
            historyLiveData.postValue(history.sortedByDescending { it.date })
        }
    }
}