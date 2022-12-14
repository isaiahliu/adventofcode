package y2016

import util.input
import kotlin.math.absoluteValue

fun main() {
    var x = 0
    var y = 0

    val directions = intArrayOf(0, 1)

    val regex = "(\\w)(\\d+)".toRegex()

    val visited = hashMapOf("0_0" to 1)
    val visitedTwice = arrayListOf<Pair<String, Int>>()

    fun visit() {
        val pos = "${x}_${y}"

        visited[pos] = (visited[pos] ?: 0) + 1

        when (visited[pos]) {
            1 -> {}
            2 -> {
                visitedTwice += pos to x.absoluteValue + y.absoluteValue
            }

            else -> {
                visitedTwice.removeIf { it.first == pos }
            }
        }
    }

    input.first().split(", ").forEach {
        val match = regex.matchEntire(it) ?: return

        val direction = match.groupValues[1]
        val steps = match.groupValues[2].toInt()

        directions.reverse()

        when (direction) {
            "L" -> {
                directions[0] = 0 - directions[0]
            }

            "R" -> {
                directions[1] = 0 - directions[1]
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

    println(x.absoluteValue + y.absoluteValue)
    println(visitedTwice.first().second)
}


