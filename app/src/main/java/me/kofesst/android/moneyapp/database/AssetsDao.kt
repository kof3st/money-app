package me.kofesst.android.moneyapp.database

import androidx.room.*
import me.kofesst.android.moneyapp.model.AssetEntity

@Dao
interface AssetsDao {

    @Query("SELECT * FROM assets WHERE assetId = :id LIMIT 1")
    suspend fun getAsset(id: Long): AssetEntity?

    @Query("SELECT * FROM assets ORDER BY assetId ASC")
    suspend fun getAssets(): List<AssetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAsset(asset: AssetEntity): Long

    @Delete
    suspend fun deleteAsset(asset: AssetEntity)

}