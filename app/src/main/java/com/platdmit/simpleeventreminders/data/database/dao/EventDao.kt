package com.platdmit.simpleeventreminders.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.platdmit.simpleeventreminders.data.database.entity.DbEvent

@Dao
interface EventDao : BaseDao<DbEvent>{
    @Query("SELECT * FROM dbevent ORDER BY id")
    fun getElements(): List<DbEvent>?

    @Query("SELECT * FROM dbevent WHERE id = :id")
    fun getElement(id: Int): DbEvent?
}