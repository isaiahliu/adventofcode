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
                val deltaX = (x2 - x1).takeIf { it != 0 }?.let { it / it.absoluteValue } ?: 0
                val deltaY = (y2 - y1).takeIf { it != 0 }?.let { it / it.absoluteValue } ?: 0

                if (!v2 && deltaX != 0 && deltaY != 0) {
                    return@forEach
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


