package com.example.testingtaskbhteam.data

import kotlinx.serialization.Serializable

@Serializable
data class TouchData(
    val x: Float,
    val y: Float,
    val pressure: Float,
    val timestamp: Long = System.currentTimeMillis()
)