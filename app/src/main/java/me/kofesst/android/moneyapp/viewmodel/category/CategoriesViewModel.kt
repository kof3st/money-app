package me.kofesst.android.moneyapp.viewmodel.category

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase

class CategoriesViewModel(
    application: Application
) : ViewModelBase(application) {
    private val _categories = MutableStateFlow(listOf<CategoryEntity>())
    val categories get() = _categories.asStateFlow()

    /**
     * Обновляет список всех категорий [categories]
     */
    fun updateCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _categories.value = categoriesDao.getCategories()
        }
    }

    /**
     * Добавляет новый счёт [category] в базу данных
     */
    fun addCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesDao.addCategory(category)
            updateCategories()
        }
    }

    /**
     * Удаляет счёт [category] из базы данных
     */
    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesDao.deleteCategory(category)
            updateCategories()
        }
    }
}