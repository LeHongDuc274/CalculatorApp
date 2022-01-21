package com.example.calculatorapp

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

        val listOperator =
            mutableListOf(ADDITION, SUBTRACTION, DIVISION, MULTIPLICATION, MODULUS, POWER)

        OPS = mutableMapOf()
        listOperator.forEach {
            OPS.put(it.value, it)
        }
    }

    fun caculShuntingYard(tokens: List<String>): List<String> {

        val output: MutableList<String> = LinkedList()
        val stack: Stack<String> = Stack()

        // For all the input tokens [S1] read the next token [S2]
        tokens.forEach { token ->
            if (OPS.containsKey(token)) {
                // Token is an operator [S3]
                while (!stack.isEmpty() && OPS.containsKey(stack.peek())) {
                    // While there is an operator (y) at the top of the operators stack and
                    // either (x) is left-associative and its precedence is less or equal to
                    // that of (y), or (x) is right-associative and its precedence
                    // is less than (y)
                    //
                    // [S4]:
                    var cOp = OPS.get(token) // Current operator
                    var lOp = OPS.get(stack.peek()) // Top operator from the stack
                    if ((cOp!!.associativity == Associativity.LEFT && cOp!!.comparePrecedence(lOp!!) <= 0) ||
                        (cOp!!.associativity == Associativity.RIGHT && cOp!!.comparePrecedence(lOp!!) < 0)
                    ) {
                        // Pop (y) from the stack S[5]
                        // Add (y) output buffer S[6]
                        output.add(stack.pop());
                        continue
                    }
                    break
                }
                // Push the new operator on the stack S[7]
                stack.push(token);
            } else if ("(".equals(token)) {
                // Else If token is left parenthesis, then push it on the stack S[8]
                stack.push(token)
            } else if (")".equals(token)) {
                // Else If the token is right parenthesis S[9]
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    // Until the top token (from the stack) is left parenthesis, pop from
                    // the stack to the output buffer
                    // S[10]
                    output.add(stack.pop())
                }
                // Also pop the left parenthesis but don't include it in the output
                // buffer S[11]
                stack.pop()
            } else {
                // Else add token to output buffer S[12]
                output.add(token)
            }
        }

        while (!stack.isEmpty()) {
            // While there are still operator tokens in the stack, pop them to output S[13]
            output.add(stack.pop())
        }
        return output
    }
}
