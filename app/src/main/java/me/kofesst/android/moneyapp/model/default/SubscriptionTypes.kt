package me.kofesst.android.moneyapp.model.default

import androidx.annotation.StringRes
import me.kofesst.android.moneyapp.R

/**
 * Перечисление типов автоплатежа.
 */
enum class SubscriptionTypes(@StringRes val titleRes: Int) {
    /**
     * Зачисляющий тип автоплатежа.
     */
    CREDIT(R.string.credit),

    /**
     * Списывающий тип автоплатежа.
     */
    DEBIT(R.string.debit)
}