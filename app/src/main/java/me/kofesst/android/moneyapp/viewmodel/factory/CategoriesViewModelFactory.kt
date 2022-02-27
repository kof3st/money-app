package me.kofesst.android.moneyapp.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.kofesst.android.moneyapp.viewmodel.CategoriesViewModel

class CategoriesViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoriesViewModel(application) as T
    }

}