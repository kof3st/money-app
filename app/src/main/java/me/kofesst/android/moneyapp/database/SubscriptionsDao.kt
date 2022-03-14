package me.kofesst.android.moneyapp.database

import androidx.room.*
import me.kofesst.android.moneyapp.model.SubscriptionEntity

@Dao
interface SubscriptionsDao {

    @Query("SELECT * FROM subscriptions WHERE subscriptionId = :id LIMIT 1")
    suspend fun getSubscription(id: Long): SubscriptionEntity?

    @Query("SELECT * FROM subscriptions ORDER BY subscriptionId ASC")
    suspend fun getSubscriptions(): List<SubscriptionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubscription(subscription: SubscriptionEntity)

    @Delete
    suspend fun deleteSubscription(subscription: SubscriptionEntity)

}