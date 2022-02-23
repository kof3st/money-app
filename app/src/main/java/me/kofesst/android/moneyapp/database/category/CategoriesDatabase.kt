package me.kofesst.android.moneyapp.database.category

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.kofesst.android.moneyapp.model.CategoryEntity

@Database(entities = [CategoryEntity::class], version = 1)
abstract class CategoriesDatabase: RoomDatabase() {
    companion object {
        private var INSTANCE: CategoriesDatabase? = null

        fun get(context: Context): CategoriesDatabase {
            if (INSTANCE != null) return INSTANCE!!

            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                CategoriesDatabase::class.java,
                "categories_db"
            ).fallbackToDestructiveMigration().build()

            return INSTANCE!!
        }
    }

    abstract fun getDao(): CategoriesDao
}