package com.example.server.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "touch_data")
@Serializable
data class TouchData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val x: Float,
    val y: Float,
    val pressure: Float,
    val timestamp: Long
)