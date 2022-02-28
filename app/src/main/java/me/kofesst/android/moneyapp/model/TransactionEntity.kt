package me.kofesst.android.moneyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val transactionId: Int = 0,
    val date: Long = Date().time,

    val categoryId: Long? = 0,
    val assetId: Long = 0,
    val targetId: Long? = null,

    val categoryName: String = "",
    val assetName: String = "",
    val targetName: String? = null,

    var title: String = "",
    var amount: Double = 0.0
)