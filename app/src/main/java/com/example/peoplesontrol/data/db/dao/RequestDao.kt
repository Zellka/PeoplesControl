package com.example.peoplesontrol.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.peoplesontrol.data.model.Request

@Dao
interface RequestDao {

    @Query("SELECT * FROM request")
    suspend fun getRequests(): List<Request>

    @Insert
    suspend fun insertRequests(requests: List<Request>)

    @Query("DELETE FROM request")
    suspend fun clearRequest()
}