package me.kofesst.android.moneyapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.kofesst.android.moneyapp.model.TransactionEntity

@Dao
interface TransactionsDao {

    @Insert
    suspend fun addTransaction(transaction: TransactionEntity)

    @Query("SELECT * from transactions ORDER BY date DESC")
    fun getTransactions(): List<TransactionEntity>

    @Query("SELECT * from transactions WHERE subscriptionId = :subscriptionId ORDER BY date DESC LIMIT 1")
    fun getSubscriptionTransaction(subscriptionId: Long): TransactionEntity?

    @Query("SELECT * from transactions ORDER BY date DESC")
    fun getTransactionsPaged(): PagingSource<Int, TransactionEntity>

}