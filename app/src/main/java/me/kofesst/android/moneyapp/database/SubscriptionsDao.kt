package me.kofesst.android.moneyapp.database

import androidx.room.*
import me.kofesst.android.moneyapp.model.SubscriptionEntity

/**
 * DataAccessObject модели [SubscriptionEntity].
 */
@Dao
interface SubscriptionsDao {

    /**
     * Возвращает объект [SubscriptionEntity] с уникальным
     * идентификатором [id]. В том случае, если объект
     * не найден, возвращает null.
     */
    @Query("SELECT * FROM subscriptions WHERE subscriptionId = :id LIMIT 1")
    suspend fun getSubscription(id: Long): SubscriptionEntity?

    /**
     * Возвращает список объектов [SubscriptionEntity].
     */
    @Query("SELECT * FROM subscriptions ORDER BY subscriptionId ASC")
    suspend fun getSubscriptions(): List<SubscriptionEntity>

    /**
     * Добавляет новый объект [SubscriptionEntity].
     * В том случае, если в базе уже есть объект
     * с таким [SubscriptionEntity.subscriptionId], запись
     * в базе обновится.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubscription(subscription: SubscriptionEntity)

    /**
     * Удаляет объект [SubscriptionEntity] из базы.
     */
    @Delete
    suspend fun deleteSubscription(subscription: SubscriptionEntity)

}