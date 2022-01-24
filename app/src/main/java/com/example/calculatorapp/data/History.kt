package com.example.calculatorapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "history_table")
class History(
    @ColumnInfo val expression: String,
    @ColumnInfo val result: String
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "history_id")
    var id: Int = 0
}
