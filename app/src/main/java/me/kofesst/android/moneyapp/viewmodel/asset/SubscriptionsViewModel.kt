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
import me.kofesst.android.moneyapp.model.SubscriptionEntity

class SubscriptionsViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val database = MainDatabase.get(application)
    private val assetsDao = database.getAssetsDao()
    private val subscriptionsDao = database.getSubscriptionsDao()

    private val _subscriptions = MutableStateFlow(listOf<SubscriptionEntity>())
    val subscriptions get() = _subscriptions.asStateFlow()

    suspend fun getAsset(id: Long): AssetEntity? {
        return assetsDao.getAsset(id)
    }

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