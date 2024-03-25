package com.bryant.itunesmusicsearch.di

import android.content.Context
import androidx.room.Room
import com.bryant.itunesmusicsearch.db.HistoryDao
import com.bryant.itunesmusicsearch.db.HistoryRoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HistoryRoomDataBase {
        return Room.databaseBuilder(
            context,
            HistoryRoomDataBase::class.java,
            "history_database"
        ).build()
    }

    @Provides
    fun provideHistoryDao(database: HistoryRoomDataBase): HistoryDao {
        return database.historyDao()
    }
}