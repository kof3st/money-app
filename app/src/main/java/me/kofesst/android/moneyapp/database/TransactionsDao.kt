package me.kofesst.android.moneyapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.model.relation.CategoryAndTransaction

@Dao
interface TransactionsDao {

    @Insert
    suspend fun addTransaction(transaction: TransactionEntity)

    @Transaction
    @Query("SELECT * from categories ORDER BY categoryId ASC")
    suspend fun getTransactions(): List<CategoryAndTransaction>

}