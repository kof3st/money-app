package me.kofesst.android.moneyapp.util

import java.util.*

fun Int.getNextDate(): Long {
    val now = Calendar.getInstance()
    val today = now.get(Calendar.DAY_OF_MONTH)

    if (today == this) return now.timeInMillis

    now.set(Calendar.DAY_OF_MONTH, this)
    if (today > this) now.add(Calendar.MONTH, 1)

    return now.timeInMillis
}

fun Long.formatDate(showTime: Boolean = true): String {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)

    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val monthName = calendar.getMonth()
    val year = calendar.get(Calendar.YEAR)

    var formatted = "%d %s %d".format(dayOfMonth, monthName, year)
    if (showTime) {
        val hours = calendar.get(Calendar.HOUR_OF_DAY).toString()
        val minutes = calendar.get(Calendar.MINUTE).toString()
        formatted = "%s в %s:%s".format(
            formatted,
            hours.padStart(2, '0'),
            minutes.padStart(2, '0')
        )
    }

    return formatted
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