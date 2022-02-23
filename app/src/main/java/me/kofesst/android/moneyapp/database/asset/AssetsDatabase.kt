package me.kofesst.android.moneyapp.database.asset

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.kofesst.android.moneyapp.model.AssetEntity

@Database(entities = [AssetEntity::class], version = 1)
abstract class AssetsDatabase: RoomDatabase() {
    companion object {
        private var INSTANCE: AssetsDatabase? = null

        fun get(context: Context): AssetsDatabase {
            if (INSTANCE != null) return INSTANCE!!

            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AssetsDatabase::class.java,
                "assets_db"
            ).fallbackToDestructiveMigration().build()

            return INSTANCE!!
        }
    }

    abstract fun getDao(): AssetsDao
}