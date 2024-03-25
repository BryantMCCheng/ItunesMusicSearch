package com.bryant.itunesmusicsearch.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1, exportSchema = false)
abstract class HistoryRoomDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}