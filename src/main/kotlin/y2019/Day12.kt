package y2019

import util.input
import kotlin.math.absoluteValue

fun main() {
    data class Moon(var x: Int, var y: Int, var z: Int) {
        var vx: Int = 0
        var vy: Int = 0
        var vz: Int = 0
        fun move() {
            x += vx
            y += vy
            z += vz
        }

        val distance: Int get() = (x.absoluteValue + y.absoluteValue + z.absoluteValue) * (vx.absoluteValue + vy.absoluteValue + vz.absoluteValue)

        val xAsString: String get() = "${x}^${vx}"
        val yAsString: String get() = "${y}^${vy}"
        val zAsString: String get() = "${z}^${vz}"
    }

    val regex = "<x=\\s*(-?\\d+), y=\\s*(-?\\d+), z=\\s*(-?\\d+)>".toRegex()

    val moons = input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() } }.map { (x, y, z) ->
        Moon(x, y, z)
    }

    val cache = hashSetOf<String>()

    var times = 0L
    var part1Result = 0

    val initialX = moons.joinToString { it.xAsString }
    val initialY = moons.joinToString { it.yAsString }
    val initialZ = moons.joinToString { it.zAsString }

    var xResult = 0L
    var yResult = 0L
    var zResult = 0L
    while (times < 1000 || xResult == 0L || yResult == 0L || zResult == 0L) {
        moons.forEach { moon ->
            moon.vx += moons.map { it.x - moon.x }.filter { it != 0 }.sumOf { it / it.absoluteValue }
            moon.vy += moons.map { it.y - moon.y }.filter { it != 0 }.sumOf { it / it.absoluteValue }
            moon.vz += moons.map { it.z - moon.z }.filter { it != 0 }.sumOf { it / it.absoluteValue }
        }

        moons.forEach { it.move() }
        times++

        if (xResult == 0L && initialX == moons.joinToString { it.xAsString }) {
            xResult = times
        }

        if (yResult == 0L && initialY == moons.joinToString { it.yAsString }) {
            yResult = times
        }

        if (zResult == 0L && initialZ == moons.joinToString { it.zAsString }) {
            zResult = times
        }

        if (times == 1000L) {
            part1Result = moons.sumOf { it.distance }
        }

    }

    println(part1Result)

    fun Long.factors(): Map<Int, Int> {
        var temp = this
        var currentFactor = 2
        val result = hashMapOf<Int, Int>()
        while (temp > 1L) {
            if (temp % currentFactor == 0L) {
                result[currentFactor] = (result[currentFactor] ?: 0) + 1
                temp /= currentFactor
            } else {
                currentFactor++
            }
        }

        return result
    }

    val t = arrayOf(xResult, yResult, zResult).map { it.factors() }

    val factors = t.map { it.keys }.flatten().toSet().associateWith { factor ->
        t.maxOf { it[factor] ?: 0 }
    }

    println(factors.entries.fold(1L) { acc, (factor, times) ->
        var t = acc
        repeat(times) {
            t *= factor
        }
        t
    })
}