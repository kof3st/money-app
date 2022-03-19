package me.kofesst.android.moneyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val transactionId: Long = 0,
    val date: Long = Date().time,

    val categoryId: Long? = 0,
    val assetId: Long = 0,
    val targetId: Long? = null,

    val categoryName: String = "",
    val assetName: String = "",
    val targetName: String? = null,

    var title: String = "",
    var amount: Double = 0.0,

    val subscriptionId: Long? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other !is TransactionEntity) return false

        return transactionId == other.transactionId &&
                date == other.date &&
                categoryId == other.categoryId &&
                assetId == other.assetId &&
                targetId == other.targetId &&
                categoryName == other.categoryName &&
                assetName == other.assetName &&
                targetName == other.targetName &&
                title == other.title &&
                amount == other.amount &&
                subscriptionId == other.subscriptionId
    }

    override fun hashCode(): Int {
        var result = transactionId.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + (categoryId?.hashCode() ?: 0)
        result = 31 * result + assetId.hashCode()
        result = 31 * result + (targetId?.hashCode() ?: 0)
        result = 31 * result + categoryName.hashCode()
        result = 31 * result + assetName.hashCode()
        result = 31 * result + (targetName?.hashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + (subscriptionId?.hashCode() ?: 0)
        return result
    }
}