package y2020

import util.input
import java.util.*

fun main() {
    fun process(addPriority: Long = 1L): Long {
        fun Any?.priority(): Long {
            return when (this) {
                "+" -> addPriority
                "*" -> 1
                else -> Long.MIN_VALUE
            }
        }

        return input.map { it.replace("(", " ( ").replace(")", " ) ").split(" ").filter { it.isNotBlank() } }
            .sumOf { nodes ->
                val resultQueue = LinkedList<Any>()
                val operatorStack = LinkedList<String>()
                nodes.forEach {
                    when (it) {
                        "+", "*" -> {
                            val priority = it.priority()
                            while (operatorStack.isNotEmpty() && priority <= operatorStack.first().priority()) {
                                resultQueue += operatorStack.pop()
                            }

                            operatorStack.push(it)
                        }

                        "(" -> {
                            operatorStack.push(it)
                        }

                        ")" -> {
                            while (operatorStack.first() != "(") {
                                resultQueue += operatorStack.pop()
                            }

                            operatorStack.pop()
                        }

                        else -> {
                            resultQueue += it.toLong()
                        }
                    }
                }

                resultQueue.addAll(operatorStack)

                val numbersStack = LinkedList<Long>()
                while (resultQueue.isNotEmpty()) {
                    when (val top = resultQueue.pop()) {
                        "+" -> {
                            val num1 = numbersStack.pop()
                            val num2 = numbersStack.pop()

                            numbersStack.push(num1 + num2)
                        }

                        "*" -> {
                            val num1 = numbersStack.pop()
                            val num2 = numbersStack.pop()

                            numbersStack.push(num1 * num2)
                        }

                        is Long -> {
                            numbersStack.push(top)
                        }
                    }
                }

                numbersStack.first()
            }
    }

    println(process())
    println(process(2))
}