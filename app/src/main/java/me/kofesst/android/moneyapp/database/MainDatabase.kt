package me.kofesst.android.moneyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.model.TransactionEntity

/**
 * Абстрактный singleton класс главной базы данных
 * приложения.
 */
@Database(
    entities = [
        CategoryEntity::class,
        AssetEntity::class,
        TransactionEntity::class,
        SubscriptionEntity::class
    ],
    version = 22,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        /**
         * Возвращает singleton экземпляр базы данных.
         * В случае, если этот метод вызывается в первый раз,
         * этот объект создаётся.
         */
        fun get(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildInstance(context).also { INSTANCE = it }
            }
        }

        /**
         * Функция, создающая экземпляр [MainDatabase].
         */
        private fun buildInstance(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MainDatabase::class.java,
            "main_database"
        ).fallbackToDestructiveMigration().build()
    }

    /**
     * Возвращает DataAccessObject модели [AssetEntity].
     */
    abstract fun getAssetsDao(): AssetsDao

    /**
     * Возвращает DataAccessObject модели [CategoryEntity].
     */
    abstract fun getCategoriesDao(): CategoriesDao

    /**
     * Возвращает DataAccessObject модели [TransactionEntity].
     */
    abstract fun getTransactionsDao(): TransactionsDao

    /**
     * Возвращает DataAccessObject модели [SubscriptionEntity].
     */
    abstract fun getSubscriptionsDao(): SubscriptionsDao
}