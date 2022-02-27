package me.kofesst.android.moneyapp.model.relation

import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.model.TransactionEntity

data class TransactionDetails(
    val category: CategoryEntity,
    val transaction: TransactionEntity,
    val asset: AssetEntity
)