package y2018

import util.input
import kotlin.math.absoluteValue

fun main() {
    data class Nanobot(val x: Int, val y: Int, val z: Int, var radius: Int) {
        var overlaps = hashSetOf<Nanobot>()

        fun distance(target: Nanobot): Int {
            return (x - target.x).absoluteValue + (y - target.y).absoluteValue + (z - target.z).absoluteValue
        }

        fun overlap(target: Nanobot): Boolean {
            return distance(target) <= radius + target.radius
        }

        fun contains(x: Int, y: Int, z: Int): Boolean {
            return (this.x - x).absoluteValue + (this.y - y).absoluteValue + (this.z - z).absoluteValue <= radius
        }

        fun possibleXRange(): IntRange {
            return x - radius..x + radius
        }

        fun possibleYRange(x: Int): IntRange {
            val r = radius - (this.x - x).absoluteValue

            return y - r..y + r
        }

        fun possibleZRange(x: Int, y: Int): IntRange {
            val r = radius - (this.x - x).absoluteValue - (this.y - y).absoluteValue

            return z - r..z + r
        }
    }

    fun IntRange.intersect(target: IntRange): IntRange {
        return this.first.coerceAtLeast(target.first)..this.last.coerceAtMost(target.last)
    }

    val regex = "pos=<(-?\\w+),(-?\\w+),(-?\\w+)>, r=(-?\\w+)".toRegex()

    val nanobots = input.mapNotNull {
        regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() }
            ?.let { (x, y, z, radius) -> Nanobot(x, y, z, radius) }
    }

    val max = nanobots.maxBy { it.radius }

    println(nanobots.count { it.distance(max) <= max.radius })

    val overlaps = Array(nanobots.size) { from ->
        BooleanArray(nanobots.size) { to ->
            if (from < to) {
                nanobots[from].overlap(nanobots[to])
            } else {
                false
            }
        }
    }

    for (index1 in nanobots.indices) {
        for (index2 in index1 + 1 until nanobots.size) {
            if (overlaps[index1][index2]) {
                nanobots[index1].overlaps += nanobots[index2]
                nanobots[index2].overlaps += nanobots[index1]
            }
        }
    }

    val temp = nanobots.toMutableList()
    while (temp.any { it.overlaps.size != temp.size - 1 }) {
        val min = temp.minBy { it.overlaps.size }
        temp.remove(min)

        temp.forEach { it.overlaps -= min }
    }

    println(temp.maxOf { (it.x.absoluteValue + it.y.absoluteValue + it.z.absoluteValue) - it.radius })
}

