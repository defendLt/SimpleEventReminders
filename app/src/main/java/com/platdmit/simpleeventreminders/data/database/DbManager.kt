package com.platdmit.simpleeventreminders.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.platdmit.simpleeventreminders.data.database.dao.EventDao
import com.platdmit.simpleeventreminders.data.database.entity.DbEvent

@Database(entities = [DbEvent::class], version = 1, exportSchema = false)
abstract class DbManager : RoomDatabase() {
    abstract fun eventsDao() : EventDao

    companion object{
        const val DATABASE_NAME: String = "simple_events"
    }
}