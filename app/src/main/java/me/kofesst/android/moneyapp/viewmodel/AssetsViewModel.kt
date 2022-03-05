package me.kofesst.android.moneyapp.viewmodel

import android.app.Application
import android.util.Log
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

    suspend fun getCategories(): List<CategoryEntity> = categoriesDao.getCategories()

    suspend fun getAsset(id: Long): AssetEntity? = assetsDao.getAsset(id)

    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionsDao.addTransaction(transaction)
        }
    }

    /**
     * Обновляет [assets], заменяя уже
     * существующие данные на данные, взятые из базы данных
     */
    fun updateAssets() {
        viewModelScope.launch(Dispatchers.IO) {
            _assets.value = assetsDao.getAssets()
        }
    }

    /**
     * Добавляет новый счёт [asset] в базу данных,
     * обновляя [assets]
     */
    fun addAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.addAsset(asset)
            updateAssets()
        }
    }

    /**
     * Обновляет счёт [asset] в базе данных,
     * обновляя [assets]
     */
    fun updateAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.addAsset(asset)
            updateAssets()
            Log.d("AAA", assets.value.toString())
        }
    }

    /**
     * Удаляет счёт [asset] из базы данных,
     * обновляя [assets]
     */
    fun deleteAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.deleteAsset(asset)
            updateAssets()
        }
    }
}