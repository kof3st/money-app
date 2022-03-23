package me.kofesst.android.moneyapp.model.state

import androidx.annotation.StringRes
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.util.*
import java.util.*

sealed class HistoryFilter(
    val id: Int,
    @StringRes val titleResId: Int
) {
    companion object {
        val FILTERS = listOf(
            DayHistoryFilter,
            WeekHistoryFilter,
            MonthHistoryFilter,
            YearHistoryFilter
        ).sortedBy { it.id }
    }

    abstract val filter: (TransactionEntity) -> Boolean

    object DayHistoryFilter : HistoryFilter(0, R.string.day) {
        override val filter: (TransactionEntity) -> Boolean
            get() = { it.date.date() compareDay Date() }
    }

    object WeekHistoryFilter : HistoryFilter(1, R.string.week) {
        override val filter: (TransactionEntity) -> Boolean
            get() = { it.date.date() compareWeek Date() }
    }

    object MonthHistoryFilter : HistoryFilter(2, R.string.month) {
        override val filter: (TransactionEntity) -> Boolean
            get() = { it.date.date() compareMonth Date() }
    }

    object YearHistoryFilter : HistoryFilter(3, R.string.year) {
        override val filter: (TransactionEntity) -> Boolean
            get() = { it.date.date() compareYear Date() }
    }
}
