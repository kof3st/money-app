package me.kofesst.android.moneyapp.util

import java.util.*

fun Long.formatDate(): String {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)

    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val monthName = calendar.getMonth()
    val year = calendar.get(Calendar.YEAR)
    val hours = calendar.get(Calendar.HOUR_OF_DAY).toString()
    val minutes = calendar.get(Calendar.MINUTE).toString()

    return "%d %s %d в %s:%s".format(
        dayOfMonth, monthName, year,
        hours.padStart(2, '0'),
        minutes.padStart(2, '0')
    )
}

fun Calendar.getMonth(): String {
    return when (this.get(Calendar.MONTH)) {
        0 -> "янв"
        1 -> "фев"
        2 -> "мар"
        3 -> "апр"
        4 -> "мая"
        5 -> "июн"
        6 -> "июл"
        7 -> "авг"
        8 -> "сен"
        9 -> "окт"
        10 -> "ноя"
        else -> "дек"
    }
}