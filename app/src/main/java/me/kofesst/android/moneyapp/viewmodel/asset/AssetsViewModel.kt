package me.kofesst.android.moneyapp.viewmodel.asset

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.kofesst.android.moneyapp.model.*
import me.kofesst.android.moneyapp.viewmodel.ViewModelBase

class AssetsViewModel(
    application: Application
) : ViewModelBase(application) {
    private val _assets = MutableStateFlow(listOf<AssetWithSubscriptions>())
    val assets get() = _assets.asStateFlow()

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
    fun getTotalBalance(): Double = assets.value.sumOf { it.asset.balance }

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