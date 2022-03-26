package me.kofesst.android.moneyapp.viewmodel.asset

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.model.*
import me.kofesst.android.moneyapp.viewmodel.ListViewModelBase

class AssetsViewModel(
    application: Application
) : ListViewModelBase<AssetWithSubscriptions>(application) {
    override suspend fun getItems(): List<AssetWithSubscriptions> = assetsDao.getAssets()

    /**
     * Добавляет новые автоплатежи [subscriptions]
     */
    fun addSubscriptions(subscriptions: List<SubscriptionEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriptions.forEach { subscription ->
                subscriptionsDao.addSubscription(subscription)
            }
        }
    }

    /**
     * Возвращает итоговый баланс, считая его из
     * всех счетов пользователя
     */
    fun getTotalBalance(): Double = items.value.sumOf { it.asset.balance }

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
     * Добавляет новый счёт [asset] в базу данных.
     */
    fun addAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.addAsset(asset)
            updateItems()
        }
    }

    /**
     * Обновляет счёт [asset] в базе данных.
     */
    fun updateAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.updateAsset(asset)
            updateItems()
        }
    }

    /**
     * Удаляет счёт [asset] из базы данных
     */
    fun deleteAsset(asset: AssetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            assetsDao.deleteAsset(asset)
            updateItems()
        }
    }
}