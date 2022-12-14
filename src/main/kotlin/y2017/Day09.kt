package y2017

import util.input

fun main() {
    val line = input.first()

    var result = 0
    var result2 = 0

    var inGarbage = false
    var ignoreNext = false

    var score = 0
    line.forEach {
        if (inGarbage) {
            if (ignoreNext) {
                ignoreNext = false
            } else {
                when (it) {
                    '!' -> {
                        ignoreNext = true
                    }

                    '>' -> {
                        inGarbage = false
                    }

                    else -> {
                        result2++
                    }
                }
            }
        } else {
            when (it) {
                '<' -> {
                    inGarbage = true
                }

                '{' -> {
                    score++
                }

                '}' -> {
                    result += score
                    score--
                }
            }
        }
    }

    println(result)
    println(result2)
}