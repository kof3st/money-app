package me.kofesst.android.moneyapp.model

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import me.kofesst.android.moneyapp.model.default.SubscriptionTypes
import java.io.Serializable

@Entity(
    tableName = "subscriptions",
    foreignKeys = [ForeignKey(
        entity = AssetEntity::class,
        parentColumns = ["assetId"],
        childColumns = ["assetId"],
        onDelete = CASCADE
    )]
)
data class SubscriptionEntity(
    @PrimaryKey(autoGenerate = true) val subscriptionId: Long = 0,
    var title: String = "",
    var amount: Double = 1.0,
    val assetId: Long = 0,
    var day: Int = 1,
    var type: Int = SubscriptionTypes.DEBIT.ordinal
) : Serializable

data class AssetWithSubscriptions(
    @Embedded val asset: AssetEntity,

    @Relation(
        parentColumn = "assetId",
        entityColumn = "assetId",
        entity = SubscriptionEntity::class
    )
    val subscriptions: List<SubscriptionEntity>
) : Serializable