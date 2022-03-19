package me.kofesst.android.moneyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.model.default.AssetTypes
import me.kofesst.android.moneyapp.util.formatWithCurrency
import java.io.Serializable

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey(autoGenerate = true) val assetId: Long = 0,
    var name: String = "",
    var balance: Double = 0.0,
    var type: Int = AssetTypes.CARD.ordinal
) : Serializable {
    companion object {
        const val NEGATIVE_BALANCE_COLOR_RES = R.color.negative
        const val NEUTRAL_BALANCE_COLOR_RES = R.color.neutral
        const val POSITIVE_BALANCE_COLOR_RES = R.color.positive
    }

    override fun toString(): String {
        return "$name (${balance.formatWithCurrency()})"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AssetEntity) return false

        return name == other.name && balance == other.balance && type == other.type
    }

    override fun hashCode(): Int {
        var result = assetId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + balance.hashCode()
        result = 31 * result + type
        return result
    }
}