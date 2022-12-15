package y2017

import util.input
import kotlin.math.absoluteValue

fun main() {
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