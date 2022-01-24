package com.example.calculatorapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calculatorapp.data.AppDataBase
import com.example.calculatorapp.data.History
import kotlinx.coroutines.*

class AppViewModel(val app : Application) : AndroidViewModel(app) {
    companion object{
        const val MAX_HISTORY_COUNT = 10
    }
    private val db = AppDataBase.getInstance(app)
    fun getCount(history: History){
         CoroutineScope(Dispatchers.IO).launch {
            val count = db.getDao().getCount()
             if (count >= MAX_HISTORY_COUNT){
                 deleteLast()
                 insert(history)
             } else insert(history)
        }
    }
    fun deleteLast(){
        CoroutineScope(Dispatchers.IO).launch {
            db.getDao().deleteLast()
        }
    }

    fun insert(history: History){
        CoroutineScope(Dispatchers.IO).launch {
            db.getDao().insert(history)
        }
    }

    fun getAll(): LiveData<List<History>> {
        return db.getDao().getAllHistory()
    }
}