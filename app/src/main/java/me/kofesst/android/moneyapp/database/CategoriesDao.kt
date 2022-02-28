package me.kofesst.android.moneyapp.database

import androidx.room.*
import me.kofesst.android.moneyapp.model.CategoryEntity

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM categories ORDER BY categoryId ASC")
    suspend fun getCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(asset: CategoryEntity): Long

    @Delete
    suspend fun deleteCategory(asset: CategoryEntity)

}