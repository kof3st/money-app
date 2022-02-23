package me.kofesst.android.moneyapp.util

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.view.recyclerview.AssetViewHolder
import java.text.DecimalFormat

fun Double.format(): String {
    val formatter = DecimalFormat().apply {
        groupingSize = 3
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }

    return formatter.format(this)
}

fun Double.formatWithCurrency(): String = "%s руб.".format(this.format())

fun Double.balanceColor(context: Context): Int {
    return when {
        this > .0 -> ContextCompat.getColor(
            context,
            AssetEntity.POSITIVE_BALANCE_COLOR_RES
        )
        this < .0 -> ContextCompat.getColor(
            context,
            AssetEntity.NEGATIVE_BALANCE_COLOR_RES
        )
        else -> ContextCompat.getColor(
            context,
            AssetEntity.NEUTRAL_BALANCE_COLOR_RES
        )
    }
}