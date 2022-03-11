package me.kofesst.android.moneyapp.viewmodel.asset

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SubscriptionsViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SubscriptionsViewModel(application) as T
    }

}