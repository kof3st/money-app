package me.kofesst.android.moneyapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.database.MainDatabase
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.model.TransactionEntity

class CategoriesViewModel(
    application: Application
): AndroidViewModel(application) {
    private val database = MainDatabase.get(application)
    private val categoriesDao = database.getCategoriesDao()
    private val transactionsDao = database.getTransactionsDao()

    val categoriesLiveData = MutableLiveData<List<CategoryEntity>>()

    /**
     * Обновляет [categoriesLiveData], заменяя уже
     * существующие данные на данные, взятые из базы данных
     */
    fun updateCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesLiveData.postValue(categoriesDao.getCategories())
        }
    }

    /**
     * Добавляет новый счёт [category] в базу данных,
     * обновляя [categoriesLiveData]
     */
    fun addCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val categoryId = categoriesDao.addCategory(category)

            updateCategories()
        }
    }

    /**
     * Обновляет счёт [category] в базе данных,
     * обновляя [categoriesLiveData]
     */
    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesDao.addCategory(category)
            updateCategories()
        }
    }

    /**
     * Удаляет счёт [category] из базы данных,
     * обновляя [categoriesLiveData]
     */
    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesDao.deleteCategory(category)
            updateCategories()
        }
    }
}