package me.kofesst.android.moneyapp.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.model.TransactionEntity

data class CategoryWithTransactions(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val transactions: List<TransactionEntity>
)