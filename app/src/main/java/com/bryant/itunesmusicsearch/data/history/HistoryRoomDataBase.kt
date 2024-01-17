package com.bryant.itunesmusicsearch.data.history

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bryant.itunesmusicsearch.utils.ApplicationContext

@Database(entities = [History::class], version = 1)
abstract class HistoryRoomDataBase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryRoomDataBase? = null

        fun getInstance(): HistoryRoomDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase().also { INSTANCE = it }
            }

        private fun buildDatabase() =
            Room.databaseBuilder(
                ApplicationContext,
                HistoryRoomDataBase::class.java,
                "search.db"
            )
                .addMigrations()
                .build()
    }
}