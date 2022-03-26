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
) : Serializable {
    val transactionAmount
        get() = amount * if (type == SubscriptionTypes.DEBIT.ordinal) -1 else 1

    override fun equals(other: Any?): Boolean {
        if (other !is SubscriptionEntity) return false

        return subscriptionId == other.subscriptionId &&
                title == other.title &&
                amount == other.amount &&
                assetId == other.assetId &&
                day == other.day &&
                type == other.type
    }

    override fun hashCode(): Int {
        var result = subscriptionId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + assetId.hashCode()
        result = 31 * result + day
        result = 31 * result + type
        return result
    }
}

data class AssetWithSubscriptions(
    @Embedded val asset: AssetEntity,

    @Relation(
        parentColumn = "assetId",
        entityColumn = "assetId",
        entity = SubscriptionEntity::class
    )
    val subscriptions: List<SubscriptionEntity>
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other !is AssetWithSubscriptions) return false

        return asset == other.asset &&
                // Check for lists equality
                subscriptions.size == other.subscriptions.size &&
                subscriptions.zip(other.subscriptions).all { (x, y) -> x == y }
    }

    override fun hashCode(): Int {
        var result = asset.hashCode()
        result = 31 * result + subscriptions.hashCode()
        return result
    }
}