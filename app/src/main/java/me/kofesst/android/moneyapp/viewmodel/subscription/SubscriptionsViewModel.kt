package me.kofesst.android.moneyapp.viewmodel.subscription

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase

class SubscriptionsViewModel(
    application: Application
) : ViewModelBase(application) {
    private val _subscriptions = MutableStateFlow(listOf<SubscriptionEntity>())
    val subscriptions get() = _subscriptions.asStateFlow()

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
     * Обновляет список автоплатежей [subscriptions]
     */
    fun updateSubscriptions() {
        viewModelScope.launch(Dispatchers.IO) {
            _subscriptions.value = subscriptionsDao.getSubscriptions()
        }
    }

    /**
     * Добавляет новый автоплатёж [subscription]
     */
    fun addSubscription(subscription: SubscriptionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriptionsDao.addSubscription(subscription)
            updateSubscriptions()
        }
    }

    /**
     * Удаляет автоплатёж [subscription]
     */
    fun deleteSubscription(subscription: SubscriptionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriptionsDao.deleteSubscription(subscription)
            updateSubscriptions()
        }
    }
}