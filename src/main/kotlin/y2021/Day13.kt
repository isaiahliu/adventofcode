package y2021

import util.input

fun main() {
    val points = hashSetOf<Pair<Int, Int>>()

    var result1 = 0
    input.mapNotNull {
        it.split(' ', ',', '=').takeLast(2).takeIf { it.size == 2 }?.let { it[0] to it[1].toInt() }
    }.forEach { (left, right) ->
        when (val x = left.toIntOrNull()) {
            null -> {
                val otherPoints = hashSetOf<Pair<Int, Int>>()
                when (left) {
                    "x" -> {
                        val t = points.filter { (x, _) -> x > right }.toSet()
                        points -= t
                        points.removeIf { it.first == right }

                        t.forEach { (x, y) ->
                            otherPoints += right * 2 - x to y
                        }
                    }

                    "y" -> {
                        val t = points.filter { (_, y) -> y > right }.toSet()
                        points -= t
                        points.removeIf { it.second == right }

                        t.forEach { (x, y) ->
                            otherPoints += x to right * 2 - y
                        }
                    }
                }

                points += otherPoints

                if (result1 == 0) {
                    result1 = points.size
                }
            }

            else -> {
                points += x to right
            }
        }
    }

    println(result1)
    for (y in points.minOf { it.second }..points.maxOf { it.second }) {
        for (x in points.minOf { it.first }..points.maxOf { it.first }) {
            print(
                if (x to y in points) {
                    "#"
                } else {
                    " "
                }
            )
        }
        println()
    }
}

