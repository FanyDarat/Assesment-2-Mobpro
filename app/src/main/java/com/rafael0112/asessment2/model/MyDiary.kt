package com.rafael0112.asessment2.model

import androidx.room.Entity

@Entity(tableName = "mydiary")
data class MyDiary(
    @PrimaryKey(autoGenerate  = true)
    val id : Long = 0L,
)