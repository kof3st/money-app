package me.kofesst.android.moneyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.model.SubscriptionEntity
import me.kofesst.android.moneyapp.model.TransactionEntity

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

        fun get(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildInstance(context).also { INSTANCE = it }
            }
        }

        private fun buildInstance(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MainDatabase::class.java,
            "main_database"
        ).fallbackToDestructiveMigration().build()
    }

    abstract fun getAssetsDao(): AssetsDao

    abstract fun getCategoriesDao(): CategoriesDao

    abstract fun getTransactionsDao(): TransactionsDao

    abstract fun getSubscriptionsDao(): SubscriptionsDao
}