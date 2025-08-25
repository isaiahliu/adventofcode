package y2016

import util.expect
import util.input
import kotlin.math.absoluteValue

fun main() {
    expect(0, 0) {
        var x = 0
        var y = 0

        val visited = hashSetOf(0 to 0)
        val visitedTwice = LinkedHashSet<Pair<Int, Int>>()
        val visitedManyTimes = hashSetOf<Pair<Int, Int>>()

        fun visit() {
            val pos = x to y

            when {
                visited.add(pos) -> {}
                pos in visitedManyTimes -> {}
                visitedTwice.remove(pos) -> visitedManyTimes += pos
                else -> visitedTwice += pos
            }
        }

        val directions = intArrayOf(0, 1)

        "(\\w)(\\d+)".toRegex().findAll(input.first()).map {
            it.groupValues[1] to it.groupValues[2].toInt()
        }.forEach { (direction, steps) ->
            directions.reverse()

            when (direction) {
                "L" -> {
                    directions[0] = -directions[0]
                }

                "R" -> {
                    directions[1] = -directions[1]
                }
            }

            repeat((directions[0] * steps).absoluteValue) {
                x += directions[0]

                visit()
            }

            repeat((directions[1] * steps).absoluteValue) {
                y += directions[1]

                visit()
            }
        }

        part1Result = x.absoluteValue + y.absoluteValue
        part2Result = visitedTwice.first().let { (x, y) -> x.absoluteValue + y.absoluteValue }
    }
}


