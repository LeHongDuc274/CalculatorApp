package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calculatorapp.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var canAddDecimal = true
    private var canAddOperation = false
    private var currentExpression = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun addStrings(num1: String, num2: String): String {
        var lenght1 = num1.length - 1
        var lenght2 = num2.length - 1
        val charArr1 = num1.toCharArray()
        val charArr2 = num2.toCharArray()

        val sb = StringBuilder()
        var remainder = 0
        while (lenght1 >= 0 || lenght2 >= 0) {
            val n1 = if (lenght1 >= 0) charArr1[lenght1--] - '0' else 0
            val n2 = if (lenght2 >= 0) charArr2[lenght2--] - '0' else 0
            val num = n1 + n2 + remainder // sum may be > 10
            remainder = num / 10 // update số nhớ
            sb.append(num % 10) //  Add result to string
        }
        if (remainder > 0) { // last remain add ahead
            sb.append(remainder)
        }
        return sb.reverse().toString()
    }

    private fun subString(n1: String, n2: String): String {
        var num1 = n1
        var num2 = n2
        val compare: Boolean
        if (num1.length > num2.length) compare = true
        else if (num1.length < num2.length) compare = false
        else compare = num1.compareTo(num2) > 0

        // sub
        var sign = "+"
        if (!compare) {
            sign = "-"
            val temp = num2
            num2 = num1
            num1 = temp
        }
        var lenght1 = num1.length - 1
        var lenght2 = num2.length - 1
        val charArr1 = num1.toCharArray()
        val charArr2 = num2.toCharArray()
        var sb = StringBuilder()
        var borrowPosition = 0
        while (lenght1 >= 0 || lenght2 >= 0) {
            val n = if (lenght1 >= 0) charArr1[lenght1--] - '0' else 0
            val m = if (lenght2 >= 0) charArr2[lenght2--] - '0' else 0
            var num = n - m - borrowPosition
            borrowPosition = 0
            if (num < 0) {
                borrowPosition = 1
                num += 10
            }
            sb.append(num)
        }
        sb = sb.reverse()
        var index = 0
        while (index < sb.length && sb[index] == '0') {
            index++
        }
        if (index == sb.length) return "0"

        return if (sign == "+")
            sb.substring(index) else sign + sb.substring(index)
    }

    private fun mulStrings(num1: String, num2: String): String {
        if ("0" == num1 || "0" == num2) return "0"
        val charArr1 = num1.toCharArray()
        val charArr2 = num2.toCharArray()
        val value = IntArray(charArr1.size + charArr2.size)
        for (i in charArr1.indices.reversed()) {
            for (j in charArr2.indices.reversed()) {
                val index = charArr1.size - 1 - i + charArr2.size - 1 - j
                value[index] += (charArr1[i] - '0') * (charArr2[j] - '0')
            }
        }
        for (i in 0 until value.size - 1) {
            value[i + 1] += value[i] / 10
            value[i] = value[i] % 10
        }
        var index = value.size - 1
        while (value[index] == 0) {
            index--
        }
        val sb = StringBuilder()
        while (index >= 0) {
            sb.append(value[index--])
        }
        return sb.toString()
    }

    private fun divStrings(num1: String, num2: String): String {
        var n1 = num1
        var n2 = num2
        var coefficient = 0
        var temp = "0"
        val compare = num1.length - num2.length
        coefficient = compare - 1
        if (coefficient > 0) {
            repeat(coefficient) {
                n2 = n2.plus("0")
            }
        } else if (coefficient < 0) {
            repeat(-coefficient) {
                n1 = n1.plus("0")
            }
        }
        var sub = n1
        for (coe in 0..coefficient) {
            //  if (coefficient > 0) {
            if (coe > 0) n2 = n2.dropLast(1)
            var canMul = true
            while (canMul) {
                sub = subString(sub, n2)
                if (sub.startsWith('-')) {
                    var absSub = sub.substring(1)
                    sub = subString(n2, absSub)
                    if (coe < coefficient) {
                        temp = mulStrings(temp, "10")
                    }
                    canMul = false
                } else {
                    temp = addStrings(temp, "1")
                }
            }
        }
        return temp
    }

    private fun rpnToResult(list: List<String>): String {
        val stack: Stack<String> = Stack()
        val ops = ShuntingYard().OPS

        list.forEach { element ->
            if (!ops.containsKey(element)) { // number -> push stack
                stack.push(element)
            } else { // operator
                val p2 = stack.pop()
                val p1 = stack.pop()
                val absP1 = p1.substring(1)
                val absP2 = p2.substring(1)
                when (element) {
                    "+" -> {
                        if (p1.startsWith('-') && p2.startsWith('-')) {
                            var value = addStrings(absP1, absP2)
                            value = "-" + value
                            stack.push(value)
                        } else if (p1.startsWith('-')) {
                            stack.push(subString(p2, absP1))
                        } else if (p2.startsWith('-')) {
                            stack.push(subString(p1, absP2))
                        } else stack.push(addStrings(p1, p2))
                    }//stack.push(p1 + p2)
                    "-" -> {
                        if (p1.startsWith('-') && p2.startsWith('-')) {
                            val value = subString(absP2, absP1)
                            stack.push(value)
                        } else if (p1.startsWith('-')) {
                            stack.push("-" + addStrings(absP1, p2))
                        } else if (p2.startsWith('-')) {
                            stack.push(addStrings(p1, absP2))
                        } else stack.push(subString(p1, p2))
                    }//stack.push(p1 - p2)
                    "*" -> {
                        if (p1.startsWith('-') && p2.startsWith('-')) {
                            var value = mulStrings(absP1, absP2)
                            stack.push(value)
                        } else if (p1.startsWith('-')) {
                            var value = mulStrings(p2, absP1)
                            value = "-" + value
                            stack.push(value)
                        } else if (p2.startsWith('-')) {
                            var value = mulStrings(p1, absP2)
                            value = "-" + value
                            stack.push(value)
                        } else stack.push(mulStrings(p1, p2))
                    }// stack.push(p1 * p2)
                    "/" -> {
                        if (p1.startsWith('-') && p2.startsWith('-')) {
                            var value = divStrings(absP1, absP2)
                            stack.push(value)
                        } else if (p1.startsWith('-')) {
                            var value = divStrings(p2, absP1)
                            value = "-" + value
                            stack.push(value)
                        } else if (p2.startsWith('-')) {
                            var value = divStrings(p1, absP2)
                            value = "-" + value
                            stack.push(value)
                        } else stack.push(divStrings(p1, p2))
                    }//stack.push(p1 / p2)
//                    "+" -> stack.push(p1 + p2)
//                    "-" -> stack.push(p1 - p2)
//                    "*" -> stack.push(p1 * p2)
//                    "/" -> stack.push(p1 / p2)
                    // "^" -> stack.push(Math.pow(p1, p2))
                }
            }
        }
        return stack.peek().toString()

    }

    fun clickNumber(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal) {
                    currentExpression.append(view.text)
                }
                canAddDecimal = false
            } else {
                currentExpression.append(view.text)
            }
            canAddOperation = true
        }
        binding.tvCurrent.text = currentExpression
    }

    fun clickOperation(view: View) {
        if (view is Button && canAddOperation) {
            Log.e("tag", view.text as String)
            when (view.text) {
                "(", ")", "+", "-", "/", "*" -> {
                    currentExpression.append(" ")
                    currentExpression.append(view.text)
                    currentExpression.append(" ")
                }
            }
            binding.tvCurrent.text = currentExpression
            canAddOperation = false
            canAddDecimal = true
        }
    }


    fun clickEquals(view: View) {
        val list: MutableList<String> = (currentExpression.split(" ")) as MutableList<String>
        list.map {
            it.trim()
        }
        val RPN = ShuntingYard().caculShuntingYard(list)
        val end = rpnToResult(RPN)
        binding.tvResult.text = end
    }

    fun clickClear(view: View) {
        binding.tvResult.text = ""
        binding.tvCurrent.text = ""
        currentExpression.clear()
        canAddDecimal = true
        canAddOperation = false
    }
    private val listOperator = listOf("+","-","/","*","^")
    fun clickUndo(view: View) {
        // if last = number -1 // last = operation -3
        if (currentExpression.length - 1 >= 0) {
            if(listOperator.contains(currentExpression.last().toString())){
                currentExpression.delete(currentExpression.length - 4,currentExpression.length-1)
            } else  currentExpression.deleteCharAt(currentExpression.length -1)
        }
        binding.tvCurrent.text = currentExpression
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}