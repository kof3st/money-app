package me.kofesst.android.moneyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    var name: String
) {
    override fun toString(): String {
        return "id: $categoryId, name: $name"
    }
}