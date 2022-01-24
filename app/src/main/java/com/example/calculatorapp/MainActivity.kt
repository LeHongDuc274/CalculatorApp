package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.calculatorapp.math.StringMath.Companion.rpnToResult
import com.example.calculatorapp.data.History
import com.example.calculatorapp.databinding.ActivityMainBinding
import com.example.calculatorapp.fragment.HistoryFragment
import com.example.calculatorapp.math.ShuntingYard
import com.example.calculatorapp.viewmodel.AppViewModel
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var canAddDecimal = true
    private var canAddOperation = false
    private var viewModel: AppViewModel? = null
    private var currentExpression = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
    }

    fun clickBracket(view: View) {
        if (view is Button) {
            if (view.text == "(") {
                canAddDecimal = true
                canAddOperation = false
            } else {
                canAddDecimal = false
                canAddOperation = true
            }
            currentExpression.add(" " + view.text + " ")
            binding.tvCurrent.text = currentExpression.joinToString("")
        }
    }

    fun clickNumber(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal) {
                    currentExpression.add("" + view.text)
                }
                canAddDecimal = false
            } else {
                currentExpression.add("" + view.text)
            }
            canAddOperation = true
        }
        binding.tvCurrent.text = currentExpression.joinToString("")
    }

    fun clickOperation(view: View) {
        if (view is Button && canAddOperation) {
            Log.e("tag", view.text as String)
            when (view.text) {
                "+", "-", "/", "*", "^" -> {
                    currentExpression.add(" " + view.text + " ")
                }
            }
            binding.tvCurrent.text = currentExpression.joinToString("")
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun clickSqrt(view: View) {
        if (view is Button) {
            currentExpression.add(" " + view.text + " ")
        }
        binding.tvCurrent.text = currentExpression.joinToString("")
        canAddOperation = false
        canAddDecimal = true
    }

    fun checkErrorBracket(sb: MutableList<String>): Boolean {
        val comp1 = sb.filter {
            it == " ( "
        }.size
        val comp2 = sb.filter {
            it == " ) "
        }.size
        return comp1 == comp2
    }

    fun clickEquals(view: View) {
        if (!checkErrorBracket(currentExpression)) {
            Toast.makeText(this, "error Bracket expression ", Toast.LENGTH_LONG).show()
            return
        }

        val tempString = currentExpression.joinToString("")
        val items = tempString.trim().split("\\s+".toRegex())

        val RPN = ShuntingYard().caculShuntingYard(items)
        try {
            val end = rpnToResult(RPN)
            binding.tvResult.text = end
            end?.let {
                insertToDB(tempString, end)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "error expression", Toast.LENGTH_SHORT).show()
        }

    }

    private fun insertToDB(tempString: String, end: String) {
        viewModel?.getCount(history = History(tempString, end))
    }

    fun clickClear(view: View) {
        binding.tvResult.text = ""
        binding.tvCurrent.text = ""
        currentExpression.clear()
        canAddDecimal = true
        canAddOperation = false
    }

    fun clickUndo(view: View) {
        // if last = number -1 // last = operation -3
        if (currentExpression.size - 1 >= 0) {
            currentExpression.removeLast()
        }
        binding.tvCurrent.text = currentExpression.joinToString("")
    }

    fun clickShowHistory(view: View) {
        val fragment = HistoryFragment()
        fragment.show(supportFragmentManager, "bottom_sheet_frag")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}