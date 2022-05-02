package me.kofesst.android.moneyapp.model.history

import androidx.annotation.StringRes
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.util.*
import java.util.*

/**
 * Фильтр истории транзакций
 */
sealed class HistoryFilter(
    val id: Int,
    @StringRes val titleResId: Int
) {
    companion object {
        val FILTERS get() = listOf(
            DayHistoryFilter,
            WeekHistoryFilter,
            MonthHistoryFilter,
            YearHistoryFilter
        ).sortedBy { it.id }
    }

    abstract val filter: (TransactionEntity) -> Boolean

    /**
     * Фильтр транзакций по текущему дню
     */
    object DayHistoryFilter : HistoryFilter(0, R.string.day) {
        override val filter: (TransactionEntity) -> Boolean
            get() = { it.date.date() compareDay Date() }
    }

    /**
     * Фильтр транзакций по текущей неделе
     */
    object WeekHistoryFilter : HistoryFilter(1, R.string.week) {
        override val filter: (TransactionEntity) -> Boolean
            get() = { it.date.date() compareWeek Date() }
    }

    /**
     * Фильтр транзакций по текущему месяцу
     */
    object MonthHistoryFilter : HistoryFilter(2, R.string.month) {
        override val filter: (TransactionEntity) -> Boolean
            get() = { it.date.date() compareMonth Date() }
    }

    /**
     * Фильтр транзакций по текущему году
     */
    object YearHistoryFilter : HistoryFilter(3, R.string.year) {
        override val filter: (TransactionEntity) -> Boolean
            get() = { it.date.date() compareYear Date() }
    }
}
