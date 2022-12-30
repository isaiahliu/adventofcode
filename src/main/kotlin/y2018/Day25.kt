package y2018

import util.input
import kotlin.math.absoluteValue

fun main() {
    val nodes = input.map { it.split(",").map { it.trim().toInt() } }

    val distanceMap = Array(nodes.size) { x ->
        IntArray(nodes.size) { y ->
            val nodeX = nodes[x]
            val nodeY = nodes[y]

            (0 until 4).sumOf { (nodeX[it] - nodeY[it]).absoluteValue }
        }
    }

    val groups = arrayListOf<MutableSet<Int>>()

    repeat(nodes.size) { new ->
        val matchGroups = groups.filter {
            it.any { distanceMap[it][new] <= 3 }
        }

        when (matchGroups.size) {
            0 -> {
                groups += hashSetOf(new)
            }

            1 -> {
                matchGroups.first() += new
            }

            else -> {
                val first = matchGroups.first()
                first += new
                matchGroups.drop(1).forEach {
                    first += it

                    groups.remove(it)
                }
            }
        }
    }

    println(groups.size)
}