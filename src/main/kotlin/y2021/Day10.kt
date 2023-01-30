package y2021

import util.input
import java.util.*

fun main() {
    val points = hashMapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137, '(' to 1, '[' to 2, '{' to 3, '<' to 4)

    var result1 = 0
    val result2 = arrayListOf<Long>()
    input.forEach {
        val stack = LinkedList<Char>()
        stack.peek()

        var incorrectChar: Char? = null
        for (c in it) {
            when (c) {
                '(', '[', '{', '<' -> stack.push(c)
                ')' -> {
                    stack.pop()?.takeIf { it != '(' }?.also {
                        incorrectChar = c
                    }
                }

                ']' -> {
                    stack.pop()?.takeIf { it != '[' }?.also {
                        incorrectChar = c
                    }
                }

                '}' -> {
                    stack.pop()?.takeIf { it != '{' }?.also {
                        incorrectChar = c
                    }
                }

                '>' -> {
                    stack.pop()?.takeIf { it != '<' }?.also {
                        incorrectChar = c
                    }
                }
            }

            if (incorrectChar != null) {
                break
            }
        }

        if (incorrectChar == null) {
            result2 += stack.fold(0L) { acc, c ->
                (acc * 5) + (points[c] ?: 0)
            }
        } else {
            incorrectChar?.let { points[it] }?.also {
                result1 += it
            }
        }
    }

    println(result1)
    println(result2.sorted().let { it[it.size / 2] })
}