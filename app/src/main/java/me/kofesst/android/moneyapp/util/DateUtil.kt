package me.kofesst.android.moneyapp.util

import java.util.*

/**
 * Возвращает дату с таким количеством
 * миллисекунд.
 */
fun Long.date(): Date {
    return Date(this)
}

/**
 * Возвращает поле даты, используя
 * класс [Calendar].
 */
fun Date.get(field: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this

    return calendar.get(field)
}

/**
 * Возвращает текущий год.
 */
fun Date.year(): Int = this.get(Calendar.YEAR)

/**
 * Возвращает текущий месяц.
 */
fun Date.month(): Int = this.get(Calendar.MONTH)

/**
 * Возвращает текущий день в месяце.
 */
fun Date.monthDay(): Int = this.get(Calendar.DAY_OF_MONTH)

/**
 * Возвращает текущий день в году.
 */
fun Date.yearDay(): Int = this.get(Calendar.DAY_OF_YEAR)

/**
 * Возвращает номер текущей недели в году.
 */
fun Date.yearWeek(): Int = this.get(Calendar.WEEK_OF_YEAR)


/**
 * Сравнивает год между двумя датами.
 */
infix fun Date.compareYear(other: Date): Boolean {
    return this.year() == other.year()
}

/**
 * Сравнивает месяц между двумя датами.
 */
infix fun Date.compareMonth(other: Date): Boolean {
    if (!(this compareYear other)) return false
    return this.month() == other.month()
}

/**
 * Сравнивает неделю между двумя датами.
 */
infix fun Date.compareWeek(other: Date): Boolean {
    if (!(this compareYear other)) return false
    return this.yearWeek() == other.yearWeek()
}

/**
 * Сравнивает день между двумя датами.
 */
infix fun Date.compareDay(other: Date): Boolean {
    if (!(this compareYear other)) return false
    return this.yearDay() == other.yearDay()
}

/**
 * Возвращает следующую дату, основываясь на
 * текущем дне.
 */
fun Int.getNextDate(): Long {
    val now = Calendar.getInstance()
    val today = now.get(Calendar.DAY_OF_MONTH)

    if (today == this) return now.timeInMillis

    now.set(Calendar.DAY_OF_MONTH, this)
    if (today > this) now.add(Calendar.MONTH, 1)

    return now.timeInMillis
}

/**
 * Форматирует дату. Если [showTime] = true,
 * в строку добавляется время.
 */
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

/**
 * Возвращает краткое название месяца.
 */
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