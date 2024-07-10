package com.example.server.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TouchDataDao {
    @Insert
    suspend fun insert(touchData: TouchData)

    @Query("DELETE FROM touch_data")
    suspend fun clearAll()

    @Query("SELECT * FROM touch_data")
    fun getAll(): Flow<List<TouchData>>
}