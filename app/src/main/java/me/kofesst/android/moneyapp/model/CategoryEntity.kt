package me.kofesst.android.moneyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Модель категории транзакции
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    var name: String
) : Serializable {
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CategoryEntity) return false

        return categoryId == other.categoryId &&
                name == other.name
    }

    override fun hashCode(): Int {
        var result = categoryId.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}