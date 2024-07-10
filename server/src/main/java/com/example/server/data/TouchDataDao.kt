package com.example.server.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TouchDataDao {
    @Insert
    suspend fun insert(touchData: TouchData)

    @Query("SELECT * FROM touch_data")
    suspend fun getAll(): List<TouchData>
}