package me.kofesst.android.moneyapp.model.default

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.kofesst.android.moneyapp.R

enum class AssetTypes(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int
) {
    CARD(R.string.card_type_title, R.drawable.ic_baseline_credit_card_24),
    CASH(R.string.cash_type_title, R.drawable.ic_baseline_account_balance_wallet_24)
}