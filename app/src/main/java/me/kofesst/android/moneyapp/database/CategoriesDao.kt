package me.kofesst.android.moneyapp.database

import androidx.room.*
import me.kofesst.android.moneyapp.model.CategoryEntity
import me.kofesst.android.moneyapp.model.relation.CategoryWithTransactions

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM categories ORDER BY categoryId ASC")
    suspend fun getCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(asset: CategoryEntity): Long

    @Delete
    suspend fun deleteCategory(asset: CategoryEntity)

    @Transaction
    @Query("SELECT * FROM categories ORDER BY categoryId ASC")
    suspend fun getCategoryWithTransactions(): List<CategoryWithTransactions>

}