package com.rafael0112.asessment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafael0112.asessment2.model.MyDiary

@Database(entities = [MyDiary::class], version = 1, exportSchema = false)
abstract class MyDiaryDb : RoomDatabase(){

    abstract val dao: MyDiaryDao

    companion object {

        @Volatile
        private var INSTANCE: MyDiaryDb? = null

        fun getInstance(context: Context): MyDiaryDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDiaryDb::class.java,
                        "mydiary.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}