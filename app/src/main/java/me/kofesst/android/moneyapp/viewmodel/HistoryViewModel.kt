package me.kofesst.android.moneyapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.database.MainDatabase
import me.kofesst.android.moneyapp.model.relation.TransactionDetails

class HistoryViewModel(
    application: Application
): AndroidViewModel(application) {
    private val database = MainDatabase.get(application)

    val historyLiveData = MutableLiveData<List<TransactionDetails>>()

    fun updateHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val history = database.getTransactionsDao().getTransactions().map { cwt ->
                val list = mutableListOf<TransactionDetails>()
                cwt.transactions.forEach {
                    list.add(TransactionDetails(
                        category = cwt.category,
                        transaction = it,
                        asset = database.getAssetsDao().getAsset(it.assetId)!!
                    ))
                }
                list
            }.flatten()

            historyLiveData.postValue(history)
            Log.d("AAA", history.toString())
        }
    }
}