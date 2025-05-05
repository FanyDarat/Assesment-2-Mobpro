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

    @Query("SELECT * FROM myDiary WHERE visible = 1 ORDER BY tanggal DESC")
    fun getDiary(): Flow<List<MyDiary>>

    @Query("SELECT * FROM myDiary WHERE visible = 0 ORDER BY tanggal DESC")
    fun getRecycleBin(): Flow<List<MyDiary>>

    @Query("SELECT * FROM myDiary WHERE id = :id")
    suspend fun getDiaryById(id: Long): MyDiary?

    @Query("DELETE FROM myDiary WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE myDiary SET visible = 0 WHERE id = :id")
    suspend fun softDeleteById(id: Long)

    @Query("UPDATE myDiary SET visible = 1 WHERE id = :id")
    suspend fun restoreById(id: Long)

    @Query("DELETE FROM myDiary WHERE visible = 0")
    suspend fun deleteAll()
}