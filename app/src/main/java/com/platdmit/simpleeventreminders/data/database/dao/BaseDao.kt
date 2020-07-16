package com.platdmit.simpleeventreminders.data.database.dao

import androidx.room.*

@Dao
interface BaseDao<Db> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbElement: Db) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(dbElements: List<Db>)

    @Update
    fun update(dbElement: Db)

    @Update
    fun updateAll(dbElements: List<Db>)

    @Delete
    fun delete(dbElement: Db)
}