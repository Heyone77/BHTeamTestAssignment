package com.example.server.data

import android.content.Context
import androidx.room.Room

object DatabaseManager {
    private var db: AppDatabase? = null

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "touch_events_db"
        ).build()
    }

    fun getDatabase(): AppDatabase {
        return db!!
    }
}