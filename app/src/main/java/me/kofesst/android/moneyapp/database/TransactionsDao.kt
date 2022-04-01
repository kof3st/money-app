package me.kofesst.android.moneyapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.kofesst.android.moneyapp.model.TransactionEntity

/**
 * DataAccessObject модели [TransactionEntity]
 */
@Dao
interface TransactionsDao {

    /**
     * Добавляет новый объект [TransactionEntity].
     * В том случае, если в базе уже есть объект
     * с таким [TransactionEntity.transactionId], запись
     * в базе обновится.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transaction: TransactionEntity)

    /**
     * Возвращает список объектов [TransactionEntity].
     */
    @Query("SELECT * from transactions ORDER BY date DESC")
    fun getTransactions(): List<TransactionEntity>

    /**
     * Возвращает объект [TransactionEntity] с самой близкой датой,
     * который принадлежит автоплатежу с уникальным идентификатором
     * [subscriptionId]. В случае, если объект не будет найден,
     * функция вернёт null.
     */
    @Query("SELECT * from transactions WHERE subscriptionId = :subscriptionId ORDER BY date DESC LIMIT 1")
    fun getSubscriptionTransaction(subscriptionId: Long): TransactionEntity?

}