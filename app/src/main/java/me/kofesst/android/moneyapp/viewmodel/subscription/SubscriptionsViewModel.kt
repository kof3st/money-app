package me.kofesst.android.moneyapp.viewmodel.subscription

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.viewmodel.ListViewModelBase

class SubscriptionsViewModel(
    application: Application
) : ListViewModelBase<SubscriptionEntity>(application) {
    override suspend fun getItems(): List<SubscriptionEntity> = subscriptionsDao.getSubscriptions()

    /**
     * Возвращает счёт с айди [id]
     */
    suspend fun getAsset(id: Long): AssetEntity? {
        return assetsDao.getAsset(id)
    }

    /**
     * Возвращает список всех счетов
     */
    suspend fun getAssets(): List<AssetEntity> {
        return assetsDao.getAssets().map { it.asset }
    }

    /**
     * Добавляет новый автоплатёж [subscription]
     */
    fun addSubscription(subscription: SubscriptionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriptionsDao.addSubscription(subscription)
            updateItems()
        }
    }

    /**
     * Удаляет автоплатёж [subscription]
     */
    fun deleteSubscription(subscription: SubscriptionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriptionsDao.deleteSubscription(subscription)
            updateItems()
        }
    }
}