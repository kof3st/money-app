package me.kofesst.android.moneyapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import me.kofesst.android.moneyapp.database.MainDatabase

/**
 * Базовый класс, представляющий все
 * [AndroidViewModel] приложения.
 */
abstract class ViewModelBase(
    application: Application
) : AndroidViewModel(application) {
    private val database = MainDatabase.get(application)
    protected val assetsDao = database.getAssetsDao()
    protected val categoriesDao = database.getCategoriesDao()
    protected val transactionsDao = database.getTransactionsDao()
    protected val subscriptionsDao = database.getSubscriptionsDao()
}