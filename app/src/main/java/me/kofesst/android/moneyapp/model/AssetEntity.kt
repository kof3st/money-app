package me.kofesst.android.moneyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.model.default.AssetTypes

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String = "",
    var balance: Double = 0.0,
    var type: Int = AssetTypes.CARD.ordinal
) {
    companion object {
        const val NEGATIVE_BALANCE_COLOR_RES = R.color.negative
        const val NEUTRAL_BALANCE_COLOR_RES  = R.color.neutral
        const val POSITIVE_BALANCE_COLOR_RES = R.color.positive
    }
}