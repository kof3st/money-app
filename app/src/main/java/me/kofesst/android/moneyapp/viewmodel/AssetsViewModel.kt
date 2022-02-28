package me.kofesst.android.moneyapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    val assetsLiveData = MutableLiveData<List<AssetEntity>>()

    /**
     * Возвращает итоговый баланс, считая его из
     * всех счетов пользователя
     */
    fun getTotalBalance(): Double = assetsLiveData.value!!.sumOf { it.balance }

    suspend fun getCategories(): List<CategoryEntity> = categoriesDao.getCategories()

    suspend fun getAsset(id: Long): AssetEntity? = assetsDao.getAsset(id)

    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionsDao.addTransaction(transaction)
        }
    }

    /**
     * Обновляет [assetsLiveData], заменяя уже
     * существующие данные на данные, взятые из базы данных
     */
    fun updateAssets() {
        viewModelScope.launch(Dispatchers.IO) {
            assetsLiveData.postValue(assetsDao.getAssets())
        }
    }

    /**
     * Добавляет новый счёт [asset] в базу данных,
     * обновляя [assetsLiveData]
     */
    fun addAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.addAsset(asset)
            updateAssets()
        }
    }

    /**
     * Обновляет счёт [asset] в базе данных,
     * обновляя [assetsLiveData]
     */
    fun updateAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.addAsset(asset)
            updateAssets()
            Log.d("AAA", assetsLiveData.value!!.toString())
        }
    }

    /**
     * Удаляет счёт [asset] из базы данных,
     * обновляя [assetsLiveData]
     */
    fun deleteAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.deleteAsset(asset)
            updateAssets()
        }
    }
}