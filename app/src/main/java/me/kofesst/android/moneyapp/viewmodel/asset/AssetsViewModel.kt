package me.kofesst.android.moneyapp.viewmodel.asset

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.database.MainDatabase
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.model.TransactionEntity

class AssetsViewModel(
    application: Application
): AndroidViewModel(application) {
    private val database = MainDatabase.get(application)
    private val assetsDao = database.getAssetsDao()
    private val categoriesDao = database.getCategoriesDao()
    private val transactionsDao = database.getTransactionsDao()

    private val _assets = MutableStateFlow(listOf<AssetEntity>())
    val assets get() = _assets.asStateFlow()

    /**
     * Возвращает итоговый баланс, считая его из
     * всех счетов пользователя
     */
    fun getTotalBalance(): Double = assets.value.sumOf { it.balance }

    /**
     * Возвращает список всех категорий
     */
    suspend fun getCategories(): List<CategoryEntity> = categoriesDao.getCategories()

    /**
     * Добавляет новую транзакцию [transaction]
     */
    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionsDao.addTransaction(transaction)
        }
    }

    /**
     * Обновляет список счетов [assets]
     */
    fun updateAssets() {
        viewModelScope.launch(Dispatchers.IO) {
            _assets.value = assetsDao.getAssets()
        }
    }

    /**
     * Добавляет новый счёт [asset] в базу данных.
     * Если счёт с таким id уже существует, то
     * обновляет его
     */
    fun addAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.addAsset(asset)
            updateAssets()
        }
    }

    /**
     * Удаляет счёт [asset] из базы данных
     */
    fun deleteAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.deleteAsset(asset)
            updateAssets()
        }
    }
}