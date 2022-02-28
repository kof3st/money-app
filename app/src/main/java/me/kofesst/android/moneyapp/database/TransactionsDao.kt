package me.kofesst.android.moneyapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.kofesst.android.moneyapp.model.TransactionEntity

@Dao
interface TransactionsDao {

    @Insert
    suspend fun addTransaction(transaction: TransactionEntity)

    @Query("SELECT * from transactions ORDER BY transactionId ASC")
    suspend fun getTransactions(): List<TransactionEntity>

}