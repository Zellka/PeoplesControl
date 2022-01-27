package com.example.peoplesontrol.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.peoplesontrol.data.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<Category>

    @Insert
    suspend fun insertCategories(categories: List<Category>)

    @Query("DELETE FROM category")
    suspend fun clearCategories()
}