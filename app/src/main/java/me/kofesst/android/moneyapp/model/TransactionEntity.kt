package me.kofesst.android.moneyapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "transactions",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = AssetEntity::class,
        parentColumns = ["assetId"],
        childColumns = ["assetId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = AssetEntity::class,
        parentColumns = ["assetId"],
        childColumns = ["targetAssetId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val transactionId: Int = 0,
    val categoryId: Long = 0,
    var assetId: Long = 0,
    var title: String = "",
    var amount: Double = 0.0,
    val date: Long = Date().time,

    // Перевод между счетами
    val targetAssetId: Long? = null
)