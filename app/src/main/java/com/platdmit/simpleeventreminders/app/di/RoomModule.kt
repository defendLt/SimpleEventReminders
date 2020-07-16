package com.platdmit.simpleeventreminders.app.di

import android.content.Context
import androidx.room.Room
import com.platdmit.simpleeventreminders.data.database.DbManager
import com.platdmit.simpleeventreminders.data.database.dao.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideEventDb(@ApplicationContext context: Context) : DbManager {
        return Room
            .databaseBuilder(
                context,
                DbManager::class.java,
                DbManager.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideEventDao( dbManager: DbManager ) : EventDao {
        return dbManager.eventsDao()
    }
}