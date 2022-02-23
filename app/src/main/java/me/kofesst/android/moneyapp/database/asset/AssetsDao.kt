package me.kofesst.android.moneyapp.database.asset

import androidx.room.*
import me.kofesst.android.moneyapp.model.AssetEntity

@Dao
interface AssetsDao {

    @Query("SELECT * FROM assets ORDER BY id ASC")
    suspend fun getAssets(): List<AssetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAsset(asset: AssetEntity)

    @Delete
    suspend fun deleteAsset(asset: AssetEntity)

}