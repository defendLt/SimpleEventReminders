package com.platdmit.simpleeventreminders.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbEvent(
    @PrimaryKey val id : Int?,
    val name: String, val desc: String, val date : Long
)