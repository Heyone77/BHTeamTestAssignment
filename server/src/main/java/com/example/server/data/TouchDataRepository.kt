package com.example.server.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    suspend fun clearAllData() {
        withContext(Dispatchers.IO) {
            touchDataDao.clearAll()
        }
    }

    fun getAllTouchData(): Flow<List<TouchData>> {
        return touchDataDao.getAll()
    }
}
