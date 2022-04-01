package me.kofesst.android.moneyapp.database

import androidx.room.*
import me.kofesst.android.moneyapp.model.CategoryEntity

/**
 * DataAccessObject модели [CategoryEntity].
 */
@Dao
interface CategoriesDao {

    /**
     * Возвращает список объектов [CategoryEntity].
     */
    @Query("SELECT * FROM categories ORDER BY categoryId ASC")
    suspend fun getCategories(): List<CategoryEntity>

    /**
     * Добавляет новый объект [CategoryEntity].
     * В том случае, если в базе уже есть объект
     * с таким [CategoryEntity.categoryId], запись
     * в базе обновится.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(asset: CategoryEntity): Long

    /**
     * Удаляет объект [CategoryEntity] из базы.
     */
    @Delete
    suspend fun deleteCategory(asset: CategoryEntity)

}