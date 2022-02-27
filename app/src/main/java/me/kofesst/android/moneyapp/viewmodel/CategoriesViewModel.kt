package me.kofesst.android.moneyapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.database.category.CategoriesDatabase
import me.kofesst.android.moneyapp.model.CategoryEntity

class CategoriesViewModel(
    application: Application
): AndroidViewModel(application) {
    private val dao = CategoriesDatabase.get(application).getDao()

    val categoriesLiveData = MutableLiveData<List<CategoryEntity>>()

    /**
     * Обновляет [categoriesLiveData], заменяя уже
     * существующие данные на данные, взятые из базы данных
     */
    fun updateCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesLiveData.postValue(dao.getCategories())
        }
    }

    /**
     * Добавляет новый счёт [category] в базу данных,
     * обновляя [categoriesLiveData]
     */
    fun addCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addCategory(category)
            updateCategories()
        }
    }

    /**
     * Обновляет счёт [category] в базе данных,
     * обновляя [categoriesLiveData]
     */
    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addCategory(category)
            updateCategories()
        }
    }

    /**
     * Удаляет счёт [category] из базы данных,
     * обновляя [categoriesLiveData]
     */
    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCategory(category)
            updateCategories()
        }
    }
}