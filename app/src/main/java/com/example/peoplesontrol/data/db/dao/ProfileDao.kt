package com.example.peoplesontrol.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.peoplesontrol.data.model.Profile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile")
    suspend fun getProfile(): Profile

    @Insert
    suspend fun insertProfile(profile: Profile)

    @Query("DELETE FROM profile")
    suspend fun clearProfile()
}