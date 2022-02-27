package me.kofesst.android.moneyapp.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.kofesst.android.moneyapp.viewmodel.HistoryViewModel

class HistoryViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(application) as T
    }

}