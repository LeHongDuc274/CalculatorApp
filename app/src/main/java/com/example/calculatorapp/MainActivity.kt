package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calculatorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var canAddDecimal = true
    private var canAddOperation = false
    private var isInsideBrackets = false
    private var result: Double = 0.0
    private var currentExpression = StringBuilder()
    private var expression = StringBuilder()
    private var prevOperator = "+"
    private var prevTemOperator = ""

    private var temResult = 0.0
    private var curNumber = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun clickNumber(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal) {
                    // currentExpression.append(view.text)
                    curNumber.append(view.text)
                    binding.tvCurrent.append(view.text)
                }
                canAddDecimal = false
            } else {
                //  currentExpression.append(view.text)
                curNumber.append(view.text)
                binding.tvCurrent.append(view.text)
            }
        }
    }

    fun clickOperation(view: View) {
        if (view is Button && curNumber.isNotEmpty()) {
            canAddDecimal = true
            when (view.text) {
                "+", "-" -> { // push up
                    pushUpAndCalcul(view.text)
                    prevTemOperator = ""
                    // currentExpression.clear()
                }
                "x", "/", "%" -> {
                    //calcul operator behind number - not push
                    calculCurrentExpresstion()
                    prevTemOperator = view.text.toString()
                    binding.tvCurrent.append(view.text)

                }
                "Sqr", "√", "^" -> {
                    //  opreator befor number
                }
                "(", ")" -> {
                    // inside bracket
                }
                "=" -> {
                }
            }
//            if (isInsideBrackets) {
//
//            } else {
//                // calculAndPushUp(view.text)
//            }
        } else{
            Toast.makeText(this,"Vui lòng nhập số hạng trước",Toast.LENGTH_SHORT).show()
        }
    }

    fun clickEquals(view: View){
        canAddDecimal = true
        if (view is Button && curNumber.isNotEmpty()) {
            pushUpAndCalcul("")
//
//            Log.e("tag",curNumber.toString())
        } else{

        }
    }
    private fun pushUpAndCalcul(operator: CharSequence) {
        when (prevOperator) {
            "+" -> result += calculCurrentExpresstion()
            "-" -> result -= calculCurrentExpresstion()
        }
        prevOperator = operator.toString()
        binding.tvExpression.append(binding.tvCurrent.text)
        binding.tvExpression.append(prevOperator)
        binding.tvResult.text = result.toString()
        binding.tvCurrent.text = ""
        //   currentExpression.clear()
        temResult = 0.0
    }


    fun calculCurrentExpresstion(): Double {
        val curNumberValue = curNumber.toString().toDouble()
        if (prevTemOperator.isEmpty()) { // first number
            temResult = curNumberValue
        } else {
            when (prevTemOperator) {
                "x" -> {
                    temResult = temResult * curNumberValue
                }
                "/" -> {
                    temResult = temResult / curNumberValue
                }
                "%" -> {
                    temResult = temResult % curNumberValue
                }
            }
        }
        curNumber.clear()
        return temResult
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}