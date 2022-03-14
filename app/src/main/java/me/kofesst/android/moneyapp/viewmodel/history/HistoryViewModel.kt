package me.kofesst.android.moneyapp.viewmodel.history

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase

class HistoryViewModel(
    application: Application
) : ViewModelBase(application) {
    val history = Pager(
        PagingConfig(
            pageSize = 5,
            prefetchDistance = 5,
            initialLoadSize = 15,
            enablePlaceholders = false
        )
    ) { transactionsDao.getTransactionsPaged() }.flow
}