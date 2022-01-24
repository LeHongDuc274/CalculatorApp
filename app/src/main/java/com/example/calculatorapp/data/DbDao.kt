package com.example.calculatorapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DbDao {

    @Insert
    suspend fun insert(history: History)

    @Query("select * from history_table order by history_id DESC")
    fun getAllHistory(): LiveData<List<History>>

    @Query("SELECT COUNT(*) FROM HISTORY_TABLE")
    fun getCount(): Int

    @Query("DELETE FROM history_table WHERE history_id=(SELECT MIN(history_id) FROM history_table)\n")
    suspend fun deleteLast()

}
