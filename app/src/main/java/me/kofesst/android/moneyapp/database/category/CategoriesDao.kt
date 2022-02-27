package me.kofesst.android.moneyapp.database.category

import androidx.room.*
import me.kofesst.android.moneyapp.model.CategoryEntity

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM categories ORDER BY id ASC")
    suspend fun getCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(asset: CategoryEntity)

    @Delete
    suspend fun deleteCategory(asset: CategoryEntity)

}