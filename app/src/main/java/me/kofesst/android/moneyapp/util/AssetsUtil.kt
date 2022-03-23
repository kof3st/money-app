package me.kofesst.android.moneyapp.util

import android.content.Context
import androidx.core.content.ContextCompat
import me.kofesst.android.moneyapp.model.AssetEntity
import java.text.DecimalFormat

fun Double.format(sign: Boolean = false): String {
    val formatter = DecimalFormat().apply {
        groupingSize = 3
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }

    val format = formatter.format(this)
    if (sign && this != 0.0) {
        return "${if (this < 0.0) "" else "+"}$format"
    }

    return format
}

fun Double.formatWithCurrency(sign: Boolean = false): String = "%s руб.".format(this.format(sign))

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