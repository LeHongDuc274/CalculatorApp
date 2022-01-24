package com.example.calculatorapp.math

import android.util.Log
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class StringMath {

    companion object {

        fun calculBitNeedTeleRight(
            n1: String,
            n2: String
        ): HashMap<String, Int> { // teleBit, delta1,delta2

            val dot1 = if (!n1.contains(".")) 0 else n1.lastIndexOf(".")
            val dot2 = if (!n2.contains(".")) 0 else n2.lastIndexOf(".")
            //pos to end
            val dotToEnd1 = if (dot1 == 0) 0 else n1.length - 1 - dot1
            val dotToEnd2 = if (dot2 == 0) 0 else n2.length - 1 - dot2
            //calcul teleBit was parse
            val teleBitRight = Math.max(dotToEnd1, dotToEnd2)
            val deta1 = teleBitRight - (dotToEnd1)
            val deta2 = teleBitRight - (dotToEnd2)

            return hashMapOf(
                "teleBit" to teleBitRight,
                "dot1" to dot1,
                "dot2" to dot2,
                "deta1" to deta1,
                "deta2" to deta2,
                "dotToEnd1" to dotToEnd1,
                "dotToEnd2" to dotToEnd2
            )
        }

        fun addStrings(n1: String, n2: String): String {
            // calcul dot (.) position and teleBit to Right
            val cal = calculBitNeedTeleRight(n1, n2)
            val dot1 = cal["dot1"]!!
            val dot2 = cal["dot2"]!!
            val deta1 = cal["deta1"]!!
            val deta2 = cal["deta2"]!!
            val teleBitRight = cal["teleBit"]!!
            //// remove
            var num1 = if (dot1 > 0) n1.removeRange(dot1..dot1) else n1
            var num2 = if (dot2 > 0) n2.removeRange(dot2..dot2) else n2
            // and add 0 at end
            if (deta1 > 0) {
                repeat(deta1) {
                    num1 += "0"
                }
            }
            if (deta2 > 0) {
                repeat(deta2) {
                    num2 += "0"
                }
            }
            //add func like a integer
            var lenght1 = num1.length - 1
            var lenght2 = num2.length - 1
            val charArr1 = num1.toCharArray()
            val charArr2 = num2.toCharArray()

            val sb = StringBuilder()
            var remainder = 0
            while (lenght1 >= 0 || lenght2 >= 0) {
                val no1 = if (lenght1 >= 0) charArr1[lenght1--] - '0' else 0
                val no2 = if (lenght2 >= 0) charArr2[lenght2--] - '0' else 0
                val num = no1 + no2 + remainder // sum may be > 10
                remainder = num / 10 // update số nhớ
                sb.append(num % 10) //  Add result to string
            }
            if (remainder > 0) { // last remain add ahead
                sb.append(remainder)
            }
            sb.reverse()
            // restore dot with teleBit allow
            if (teleBitRight > 0) sb.insert(sb.length - teleBitRight, ".")
            return sb.toString()
        }

        fun subString(n1: String, n2: String): String {
            // calcul dot (.) position and teleBit to Right
            val cal = calculBitNeedTeleRight(n1, n2)
            val dot1 = cal["dot1"]!!
            val dot2 = cal["dot2"]!!
            val deta1 = cal["deta1"]!!
            val deta2 = cal["deta2"]!!
            val teleBitRight = cal["teleBit"]!!
            //// remove
            var num1 = if (dot1 > 0) n1.removeRange(dot1..dot1) else n1
            var num2 = if (dot2 > 0) n2.removeRange(dot2..dot2) else n2
            // and add 0 at end
            if (deta1 > 0) {
                repeat(deta1) {
                    num1 += "0"
                }
            }
            if (deta2 > 0) {
                repeat(deta2) {
                    num2 += "0"
                }
            }
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
            if (teleBitRight > 0) sb.insert(sb.length - teleBitRight, ".")
            var index = 0
            while (index < sb.length && sb[index] == '0') {
                index++
            }
            if (index == sb.length) return "0"

            var result = sb.substring(index)
            if (result.startsWith(".")) {
                result = "0" + result
            }
            return if (sign == "+") {
                result
            } else {
                sign + result
            }
        }

        fun mulStrings(n1: String, n2: String): String {
            if ("0" == n1 || "0" == n2) return "0"
            //
            val cal = calculBitNeedTeleRight(n1, n2)
            val dot1 = cal["dot1"]!!
            val dot2 = cal["dot2"]!!
            val teleBitRight = cal["dotToEnd1"]!! + cal["dotToEnd2"]!!
            //// remove
            val num1 = if (dot1 > 0) n1.removeRange(dot1..dot1) else n1
            val num2 = if (dot2 > 0) n2.removeRange(dot2..dot2) else n2


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
            if (teleBitRight > 0) sb.insert(sb.length - teleBitRight, ".")
            return sb.toString()
        }

        fun divStrings(num1: String, num2: String, accuracy: Int = 3): String {
            // check num2 == 0.0
            var index = 0
            while (index < num2.length && num2[index] == '0') {
                index++
            }
            if (index == num2.length || (index == num2.length - 1 && num2.contains("."))) {
                throw Exception("Khong chia dc cho 0.0")
            }

            val cal = calculBitNeedTeleRight(num1, num2)
            val dot1 = cal["dot1"]!!
            val dot2 = cal["dot2"]!!
            val deta1 = cal["deta1"]!!
            val deta2 = cal["deta2"]!!
            val teleBitRight = cal["teleBit"]!!
            //// remove
            var n1 = if (dot1 > 0) num1.removeRange(dot1..dot1) else num1
            var n2 = if (dot2 > 0) num2.removeRange(dot2..dot2) else num2
            if (deta1 > 0) {
                repeat(deta1) {
                    n1 += "0"
                }
            }
            if (deta2 > 0) {
                repeat(deta2) {
                    n2 += "0"
                }
            }
            repeat(accuracy) {
                n1 += "0"
            }
            var coefficient = 0
            var temp = "0"
            val compare = n1.length - n2.length
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
            if (temp.length > accuracy) temp = temp.substring(
                0,
                temp.length - accuracy
            ) + "." + temp.substring(temp.length - accuracy)
//            while (temp.last() == ' ') {
//                temp = temp.substring(0, temp.length - 2)
//            }
            else {
                val size = temp.length
                repeat(accuracy - size) {
                    temp = "0" + temp
                }
                temp = "0." + temp
            }
            return temp
        }

        fun rpnToResult(list: List<String>): String? {
            val stack: Stack<String> = Stack()
            val ops = ShuntingYard().OPS

            list.forEach { element ->
                if (!ops.containsKey(element)) { // number -> push stack
                    stack.push(element)
                } else { // operator
                    if (element == "sqrt") {
                        val p = stack.pop()
                        var n = p.toDoubleOrNull()
                        if (n != null) {
                            stack.push(Math.sqrt(n).toString())
                        }

                    } else {
                        val p2 = stack.pop()
                        val p1 = stack.pop()
                        val absP1 = if (p1.length > 1) p1.substring(1) else p1
                        val absP2 = if (p2.length > 1) p2.substring(1) else p2
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
                            }
                            "^" -> {
                                var n1 = p1.toDoubleOrNull()
                                var n2 = p2.toDoubleOrNull()
                                if (n1 != null && n2 != null) {
                                    stack.push(Math.pow(n1, n2).toString())
                                }
                            }
                        }
                        //stack.push(p1 / p2)
//                    "+" -> stack.push(p1 + p2)
//                    "-" -> stack.push(p1 - p2)
//                    "*" -> stack.push(p1 * p2)
//                    "/" -> stack.push(p1 / p2)
                        // "^" -> stack.push(Math.pow(p1, p2))
                    }
                }
            }
            return if (stack.isEmpty()) null else  stack.peek().toString()
        }

//        fun findSurplus(num1: String, num2 :String){
//            var n1 = num1
//            var n2 = num2
//            var coefficient = 0
//            var temp = "0"
//            val compare = n1.length - n2.length
//            coefficient = compare - 1
//            if (coefficient > 0) {
//                repeat(coefficient) {
//                    n2 = n2.plus("0")
//                }
//            } else if (coefficient < 0) {
//                repeat(-coefficient) {
//                    n1 = n1.plus("0")
//                }
//            }
//            var sub = n1
//            for (coe in 0..coefficient) {
//                //  if (coefficient > 0) {
//                if (coe > 0) n2 = n2.dropLast(1)
//                var canMul = true
//                while (canMul) {
//                    sub = subString(sub, n2)
//                    if (sub.startsWith('-')) {
//                        var absSub = sub.substring(1)
//                        sub = subString(n2, absSub)
//                        if (coe < coefficient) {
//                            temp = mulStrings(temp, "10")
//                        }
//                        canMul = false
//                    } else {
//                        temp = addStrings(temp, "1")
//                    }
//                }
//            }
//           var surPlus = if()
//            return temp
//        }


        fun find2(num2: String): Pair<Int, Int?> {
            var count = 0
            var n2 = num2
            n2 = divStrings(n2, "2", 1)
            while (!n2.startsWith("0")) {
                n2 = divStrings(n2, "2", 1)
                count++
                Log.e("test1", n2.toString())
            }
            Log.e("test1", count.toString())

            val surPlus = subString(num2, n2).toIntOrNull()
            return Pair(count, surPlus)
        }

        fun powerStrings(num1: String, num2: String): String {
            var curNumber: String = num1
            val pair = find2(num2)
            val count = pair.first
            val surPlus = pair.second
            if (count == 0 && surPlus == 1) return num1
            if (count == 0 && surPlus == 0) return "1"
            var prevNum = curNumber
            for (i in 1..count) {
                prevNum = curNumber
                curNumber = mulStrings(curNumber, curNumber)
                Log.e("test2", curNumber)
            }
            curNumber = mulStrings(curNumber, powerStrings(num1, surPlus.toString()))
            return curNumber
        }
    }
}