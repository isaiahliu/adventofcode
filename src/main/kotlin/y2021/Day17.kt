package y2021

import util.input
import kotlin.math.absoluteValue

fun main() {
    val (x1, x2, y1, y2) = "target area: x=(-?\\d+)..(-?\\d+), y=(-?\\d+)..(-?\\d+)".toRegex()
        .matchEntire(input.first())!!.groupValues.drop(1).map { it.toInt() }

    var result1 = 0
    val result2 = hashSetOf<Pair<Int, Int>>()
    for (y in -y1.absoluteValue until y1.absoluteValue) {
        var sec = if (y > 0) y * 2 + 1 else 0
        var v = y.absoluteValue
        if (y > 0) {
            v++
        }
        var t = 0

        val seconds = hashSetOf<Int>()

        while (true) {
            sec++
            t += v

            if (t > y1.absoluteValue) {
                break
            }

            if (t >= y2.absoluteValue) {
                seconds += sec
            }
            v++
        }

        seconds.forEach { second ->
            var x = 1

            while (true) {
                var currentX = x
                var remainingSeconds = second

                var totalX = 0
                while (currentX > 0 && remainingSeconds > 0) {
                    totalX += currentX

                    currentX--
                    remainingSeconds--
                }

                when {
                    totalX < x1 -> {
                        x++
                        continue
                    }

                    totalX > x2 -> {
                        break
                    }

                    else -> {
                        if (y > 0) {
                            result1 = result1.coerceAtLeast((1 + y) * y / 2)
                        }
                        result2 += x to y
                        x++
                        continue
                    }
                }
            }

        }
    }

    println(result1)
    println(result2.size)
}