package me.kofesst.android.moneyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.kofesst.android.moneyapp.model.*

@Database(
    entities = [
        CategoryEntity::class,
        AssetEntity::class,
        TransactionEntity::class,
        SubscriptionEntity::class
    ],
    version = 16,
    exportSchema = false
)
abstract class MainDatabase: RoomDatabase() {
    companion object {
        private var INSTANCE: MainDatabase? = null

        fun get(context: Context): MainDatabase {
            if (INSTANCE != null) return INSTANCE!!

            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MainDatabase::class.java,
                "main_database"
            ).fallbackToDestructiveMigration().build()

            return INSTANCE!!
        }
    }

    abstract fun getAssetsDao(): AssetsDao

    abstract fun getCategoriesDao(): CategoriesDao

    abstract fun getTransactionsDao(): TransactionsDao

    abstract fun getSubscriptionsDao(): SubscriptionsDao
}