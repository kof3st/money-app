package me.kofesst.android.moneyapp.database

import androidx.room.*
import me.kofesst.android.moneyapp.model.AssetEntity
import me.kofesst.android.moneyapp.model.AssetWithSubscriptions

/**
 * DataAccessObject модели [AssetEntity].
 */
@Dao
interface AssetsDao {

    /**
     * Возвращает объект [AssetEntity] с уникальным
     * идентификатором [id]. В том случае, если объект
     * не найден, возвращает null.
     */
    @Query("SELECT * FROM assets WHERE assetId = :id LIMIT 1")
    suspend fun getAsset(id: Long): AssetEntity?

    /**
     * Возвращает список объектов [AssetWithSubscriptions].
     */
    @Transaction
    @Query("SELECT * FROM assets ORDER BY assetId ASC")
    suspend fun getAssets(): List<AssetWithSubscriptions>

    /**
     * Добавляет новый объект [AssetEntity].
     * В том случае, если в базе уже есть объект
     * с таким [AssetEntity.assetId], добавления не
     * произойдёт.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addAsset(asset: AssetEntity): Long

    /**
     * Обновляет объект [AssetEntity] в базе
     * по [AssetEntity.assetId].
     */
    @Update
    suspend fun updateAsset(asset: AssetEntity)

    /**
     * Удаляет объект [AssetEntity] из базы.
     */
    @Delete
    suspend fun deleteAsset(asset: AssetEntity)

}