package y2017

import util.input
import kotlin.math.absoluteValue

fun main() {
    solve0()
    solve1()
}

private fun solve0() {
    var rotation = 0
    var distance = 0
    var offset = 0
    var max = 0
    input.first().split(",").forEach {
        val direction = when (it) {
            "n" -> 0
            "ne" -> 1
            "se" -> 2
            "s" -> 3
            "sw" -> 4
            else -> 5
        }

        if (distance == 0) {
            rotation = direction
        }

        when ((direction - rotation).mod(6)) {
            0 -> {
                distance++
            }

            1 -> {
                distance++
                offset++
            }

            2 -> {
                offset++
            }

            3 -> {
                distance--
            }

            4 -> {
                distance--
                offset--
            }

            5 -> {
                offset--
            }
        }

        if (offset < 0) {
            rotation = (rotation - 1).mod(6)
            distance++
            offset = distance - 1
        } else if (offset >= distance) {
            rotation = (rotation + 1) % 6
            offset = 0
        }

        max = max.coerceAtLeast(distance)
    }

    println(distance)
    println(max)
}

private fun solve1() {
    val xyz = intArrayOf(0, 0, 0)

    var max = 0
    var current = 0
    input.first().split(",").forEach {
        when (it) {
            "n" -> xyz[0]++
            "ne" -> xyz[2]--
            "se" -> xyz[1]++
            "s" -> xyz[0]--
            "sw" -> xyz[2]++
            else -> xyz[1]--
        }

        val middle = xyz.sorted()[1]
        current = xyz.sumOf { (it - middle).absoluteValue }

        max = max.coerceAtLeast(current)
    }

    println(current)
    println(max)
}