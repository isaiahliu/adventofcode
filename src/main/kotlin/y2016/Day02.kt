package y2016

import util.input
import kotlin.math.absoluteValue

fun main() {
    val keyboard1 = Array(3) { x ->
        IntArray(3) { y ->
            x * 3 + y + 1
        }
    }

    var num = 1
    val keyboard2 = Array(5) { x ->
        Array(5) { y ->
            if ((x - 2).absoluteValue + (y - 2).absoluteValue <= 2) {
                (num++).toString(16).uppercase()
            } else {
                ""
            }
        }
    }

    var x1 = 1
    var y1 = 1
    var x2 = 2
    var y2 = 0

    val result1 = StringBuilder()
    val result2 = StringBuilder()

    input.forEach {
        it.forEach {
            when (it) {
                'L' -> {
                    y1 = (y1 - 1).coerceAtLeast(0)
                    y2 = (y2 - 1).coerceAtLeast((x2 - 2).absoluteValue)
                }

                'R' -> {
                    y1 = (y1 + 1).coerceAtMost(2)
                    y2 = (y2 + 1).coerceAtMost(4 - (x2 - 2).absoluteValue)
                }

                'U' -> {
                    x1 = (x1 - 1).coerceAtLeast(0)
                    x2 = (x2 - 1).coerceAtLeast((y2 - 2).absoluteValue)
                }

                'D' -> {
                    x1 = (x1 + 1).coerceAtMost(2)
                    x2 = (x2 + 1).coerceAtMost(4 - (y2 - 2).absoluteValue)
                }
            }
        }

        result1.append(keyboard1[x1][y1])
        result2.append(keyboard2[x2][y2])
    }

    println(result1)
    println(result2)
}