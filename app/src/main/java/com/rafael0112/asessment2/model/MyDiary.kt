package com.rafael0112.asessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mydiary")
data class MyDiary(
    @PrimaryKey(autoGenerate  = true)
    val id : Long = 0L,
    val mood: String,
    val judul : String,
    val catatan : String,
    val tanggal : String,
    val visible : Boolean = true
)