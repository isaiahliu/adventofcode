package y2021

import util.input
import kotlin.math.absoluteValue

fun main() {
    fun process(v2: Boolean): Int {
        val covers = hashSetOf<Pair<Int, Int>>()
        val dangerous = hashSetOf<Pair<Int, Int>>()

        fun mark(point: Pair<Int, Int>) {
            if (!covers.add(point)) {
                dangerous += point
            }
        }

        val regex = "(\\d+),(\\d+) -> (\\d+),(\\d+)".toRegex()
        input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() } }
            .forEach { (x1, y1, x2, y2) ->
                var deltaX = 0
                var deltaY = 0
                when {
                    x1 == x2 -> {
                        deltaY = (y2 - y1).let { it / it.absoluteValue }
                    }

                    y1 == y2 -> {
                        deltaX = (x2 - x1).let { it / it.absoluteValue }
                    }

                    else -> {
                        if (v2) {
                            deltaY = (y2 - y1).let { it / it.absoluteValue }
                            deltaX = (x2 - x1).let { it / it.absoluteValue }
                        } else {
                            return@forEach
                        }
                    }
                }

                mark(x1 to y1)

                var tempX = x1
                var tempY = y1

                do {
                    tempX += deltaX
                    tempY += deltaY

                    mark(tempX to tempY)
                } while (tempX != x2 || tempY != y2)
            }

        return dangerous.size
    }

    println(process(false))
    println(process(true))
}


