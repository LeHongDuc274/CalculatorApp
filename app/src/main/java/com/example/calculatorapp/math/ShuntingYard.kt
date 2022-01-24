package com.example.calculatorapp.math

import com.example.calculatorapp.Associativity
import com.example.calculatorapp.Operator
import java.util.*

class ShuntingYard() {
    var OPS: MutableMap<String, Operator>

    init {
        val ADDITION = Operator("+", Associativity.LEFT, 0)
        val SUBTRACTION = Operator("-", Associativity.LEFT, 0)
        val DIVISION = Operator("/", Associativity.LEFT, 5)
        val MULTIPLICATION = Operator("*", Associativity.LEFT, 5)
        val MODULUS = Operator("%", Associativity.LEFT, 5)
        val POWER = Operator("^", Associativity.RIGHT, 10)
        val SQRT = Operator("sqrt", Associativity.RIGHT, 10)

        val listOperator =
            mutableListOf(ADDITION, SUBTRACTION, DIVISION, MULTIPLICATION, MODULUS, POWER, SQRT)

        OPS = mutableMapOf()
        listOperator.forEach {
            OPS.put(it.value, it)
        }
    }

    fun caculShuntingYard(tokens: List<String>): List<String> {

        val output: MutableList<String> = LinkedList()
        val stack: Stack<String> = Stack()
        tokens.forEach { token ->
            if (OPS.containsKey(token)) {
                // Token is an operator [S3]
                while (!stack.isEmpty() && OPS.containsKey(stack.peek())) {
                    var cOp = OPS.get(token) // Current operator
                    var lOp = OPS.get(stack.peek()) // Top operator in stack
                    if ((cOp!!.associativity == Associativity.LEFT && cOp!!.comparePrecedence(lOp!!) <= 0) ||
                        (cOp!!.associativity == Associativity.RIGHT && cOp!!.comparePrecedence(lOp!!) < 0)
                    ) {
                        output.add(stack.pop());
                        continue
                    }
                    break
                }
                stack.push(token);
            } else if ("(".equals(token)) {
                stack.push(token)
            } else if (")".equals(token)) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop())
                }
                if (stack.isNotEmpty()) stack.pop()
            } else {
                output.add(token)
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop())
        }
        return output
    }
}
