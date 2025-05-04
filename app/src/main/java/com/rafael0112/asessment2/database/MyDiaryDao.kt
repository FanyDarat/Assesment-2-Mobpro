package com.rafael0112.asessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rafael0112.asessment2.model.MyDiary
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDiaryDao {


    @Insert
    suspend fun insert(myDiary: MyDiary)

    @Update
    suspend fun update(myDiary: MyDiary)

    @Query("SELECT * FROM myDiary ORDER BY tanggal DESC")
    fun getDiary(): Flow<List<MyDiary>>

    @Query("SELECT * FROM myDiary WHERE id = :id")
    suspend fun getDiaryById(id: Long): MyDiary?

    @Query("DELETE FROM myDiary WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE myDiary SET visible = false WHERE id = :id")
    suspend fun softDeleteById(id: Long)
}