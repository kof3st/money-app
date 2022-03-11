package me.kofesst.android.moneyapp.model.default

import androidx.annotation.StringRes
import me.kofesst.android.moneyapp.R

enum class SubscriptionTypes(@StringRes val titleRes: Int) {
    CREDIT(R.string.credit),
    DEBIT(R.string.debit)
}