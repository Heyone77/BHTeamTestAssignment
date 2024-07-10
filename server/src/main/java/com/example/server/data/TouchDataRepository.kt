package com.example.server

import com.example.server.data.TouchData
import com.example.server.data.TouchDataDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TouchDataRepository @Inject constructor(
    private val touchDataDao: TouchDataDao
) {
    suspend fun saveTouchData(touchData: TouchData) {
        withContext(Dispatchers.IO) {
            touchDataDao.insert(touchData)
        }
    }
}