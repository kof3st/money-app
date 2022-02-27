package me.kofesst.android.moneyapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.database.MainDatabase
import me.kofesst.android.moneyapp.model.AssetEntity

class AssetsViewModel(
    application: Application
): AndroidViewModel(application) {
    private val dao = MainDatabase.get(application).getAssetsDao()

    val assetsLiveData = MutableLiveData<List<AssetEntity>>()

    /**
     * Возвращает итоговый баланс, считая его из
     * всех счетов пользователя
     */
    fun getTotalBalance(): Double = assetsLiveData.value!!.sumOf { it.balance }

    /**
     * Обновляет [assetsLiveData], заменяя уже
     * существующие данные на данные, взятые из базы данных
     */
    fun updateAssets() {
        viewModelScope.launch(Dispatchers.IO) {
            assetsLiveData.postValue(dao.getAssets())
        }
    }

    /**
     * Добавляет новый счёт [asset] в базу данных,
     * обновляя [assetsLiveData]
     */
    fun addAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addAsset(asset)
            updateAssets()
        }
    }

    /**
     * Обновляет счёт [asset] в базе данных,
     * обновляя [assetsLiveData]
     */
    fun updateAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addAsset(asset)
            updateAssets()
        }
    }

    /**
     * Удаляет счёт [asset] из базы данных,
     * обновляя [assetsLiveData]
     */
    fun deleteAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAsset(asset)
            updateAssets()
        }
    }
}