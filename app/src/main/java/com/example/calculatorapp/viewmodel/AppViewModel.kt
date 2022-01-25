package com.example.calculatorapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calculatorapp.data.AppDataBase
import com.example.calculatorapp.data.History
import com.example.calculatorapp.math.ShuntingYard
import com.example.calculatorapp.math.StringMath
import kotlinx.coroutines.*
import java.lang.Exception

class AppViewModel(val app: Application) : AndroidViewModel(app) {
    companion object {
        const val MAX_HISTORY_COUNT = 10
    }

    private var canAddDecimal = true
    private var canAddOperation = false
    private var currentExpression = mutableListOf<String>()
    var textExpression = MutableLiveData<String>("")
    var message = MutableLiveData<String>("")
    var textResult = MutableLiveData<String>("")
    private fun updateExpression() {
        textExpression.value = currentExpression.joinToString("")
    }

    fun onBracket(str: String) {
        if (str == "(") {
            canAddDecimal = true
            canAddOperation = false
        } else {
            canAddDecimal = false
            canAddOperation = true
        }
        currentExpression.add(" " + str + " ")
        updateExpression()
    }

    fun onNumber(text: String) {
        if (text == ".") {
            if (canAddDecimal) {
                currentExpression.add("" + text)
            }
            canAddDecimal = false
        } else {
            currentExpression.add("" + text)
        }
        canAddOperation = true
        updateExpression()
    }

    fun onOperation(text: String) {
        if (canAddOperation) {
          //  Log.e("tag", view.text as String)
            when (text) {
                "+", "-", "/", "*", "^" -> {
                    currentExpression.add(" " + text + " ")
                }
            }
            updateExpression()
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun onSqrt(text: String) {
        currentExpression.add(" " + text + " ")
        canAddOperation = false
        canAddDecimal = true
        updateExpression()
    }

    private fun checkErrorBracket(sb: MutableList<String>): Boolean {
        val comp1 = sb.filter {
            it == " ( "
        }.size
        val comp2 = sb.filter {
            it == " ) "
        }.size
        return comp1 == comp2
    }

    fun onEquals() {

        if (!checkErrorBracket(currentExpression)) {
            message.value = "error Bracket expression"
            return
        }
        val tempString = currentExpression.joinToString("")
        val items = tempString.trim().split("\\s+".toRegex())
        val RPN = ShuntingYard().caculShuntingYard(items)
        try {
            val end = StringMath.rpnToResult(RPN)
            end?.let {
                textResult.value = it
                insertToDB(tempString, end)
            }
        } catch (e: Exception) {
            message.value = "error expression"
        }
    }

    fun onClear() {
        textExpression.value = ""
        textResult.value = ""
        currentExpression.clear()
        canAddDecimal = true
        canAddOperation = false
    }

    fun onUndo() {
        // if last = number -1 // last = operation -3
        if (currentExpression.size - 1 >= 0) {
            currentExpression.removeLast()
        }
        updateExpression()
    }

    private fun insertToDB(tempString: String, end: String) {
        getCount(history = History(tempString, end))
    }

    private val db = AppDataBase.getInstance(app)
    fun getCount(history: History) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = db.getDao().getCount()
            if (count >= MAX_HISTORY_COUNT) {
                deleteLast()
                insert(history)
            } else insert(history)
        }
    }

    fun deleteLast() {
        CoroutineScope(Dispatchers.IO).launch {
            db.getDao().deleteLast()
        }
    }

    fun insert(history: History) {
        CoroutineScope(Dispatchers.IO).launch {
            db.getDao().insert(history)
        }
    }

    fun getAll(): LiveData<List<History>> {
        return db.getDao().getAllHistory()
    }
}