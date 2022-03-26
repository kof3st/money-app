package me.kofesst.android.moneyapp.viewmodel.category

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.viewmodel.ListViewModelBase

class CategoriesViewModel(
    application: Application
) : ListViewModelBase<CategoryEntity>(application) {
    override suspend fun getItems(): List<CategoryEntity> = categoriesDao.getCategories()

    /**
     * Добавляет новый счёт [category] в базу данных
     */
    fun addCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesDao.addCategory(category)
            updateItems()
        }
    }

    /**
     * Удаляет счёт [category] из базы данных
     */
    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesDao.deleteCategory(category)
            updateItems()
        }
    }
}