package com.example.calculatorapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class],version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getDao(): DbDao

    companion object{
        @Volatile
        private var instance : AppDataBase? = null

        fun getInstance(context : Context): AppDataBase{
            if(instance == null){
                instance = Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    "appDatabase"
                ).build()
            }
            return instance!!
        }
    }
}