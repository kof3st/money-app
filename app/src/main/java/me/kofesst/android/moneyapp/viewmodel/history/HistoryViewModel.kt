package me.kofesst.android.moneyapp.viewmodel.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import me.kofesst.android.moneyapp.database.MainDatabase

class HistoryViewModel(
    application: Application
): AndroidViewModel(application) {
    private val database = MainDatabase.get(application)
    private val transactionsDao = database.getTransactionsDao()

    val history = Pager(
        PagingConfig(
            pageSize = 5,
            prefetchDistance = 5,
            initialLoadSize = 15,
            enablePlaceholders = false
        )
    ) { transactionsDao.getTransactions() }.flow
}