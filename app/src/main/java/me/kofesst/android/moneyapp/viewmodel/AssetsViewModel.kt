package me.kofesst.android.moneyapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.database.asset.AssetsDatabase
import me.kofesst.android.moneyapp.model.AssetEntity

class AssetsViewModel(
    application: Application
): AndroidViewModel(application) {
    private val dao = AssetsDatabase.get(application).getDao()

    private val assets = mutableListOf<AssetEntity>()
    val assetsLiveData = MutableLiveData<List<AssetEntity>>()

    /**
     * Возвращает итоговый баланс, считая его из
     * всех счетов пользователя
     */
    fun getTotalBalance(): Double = assets.sumOf { it.balance }

    /**
     * Обновляет [assetsLiveData], заменяя уже
     * существующие данные на данные, взятые из базы данных
     */
    fun updateAssets() {
        viewModelScope.launch(Dispatchers.IO) {
            assets.clear()
            assets.addAll(dao.getAssets())
            assetsLiveData.postValue(assets)
        }
    }

    /**
     * Добавляет новый счёт [asset] в базу данных,
     * обновляя [assetsLiveData].
     * Также вставляет элемент на позицию [listPosition],
     * если параметр функции передан
     */
    fun addAsset(asset: AssetEntity, listPosition: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addAsset(asset)
            assets.add(listPosition ?: assets.size, asset)
            assetsLiveData.postValue(assets)
        }
    }

    /**
     * Удаляет счёт [asset] из базы данных,
     * обновляя [assetsLiveData]
     */
    fun deleteAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAsset(asset)
            assets.remove(asset)
            assetsLiveData.postValue(assets)
        }
    }
}