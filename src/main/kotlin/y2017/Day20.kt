package y2017

import util.input
import kotlin.math.absoluteValue

fun main() {
    val regex =
        "p=<\\s*(-?\\d+),\\s*(-?\\d+),\\s*(-?\\d+)>, v=<\\s*(-?\\d+),\\s*(-?\\d+),\\s*(-?\\d+)>, a=<\\s*(-?\\d+),\\s*(-?\\d+),\\s*(-?\\d+)>".toRegex()

    val particles = input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() } }
        .mapIndexed { particleIndex, data ->
            Day20Particle(
                particleIndex,
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                data[5],
                data[6],
                data[7],
                data[8]
            )
        }

    println(particles.minBy { it.ax.absoluteValue + it.ay.absoluteValue + it.az.absoluteValue }.index)

    fun collide(p1: Day20Particle, p2: Day20Particle): Int {
        var impossible = false
        var distance = p1.distanceOf(p2)
        var speedingUp = p1.speedingUp() && p2.speedingUp()
        var turn = 0

        while (!impossible) {
            p1.move()
            p2.move()
            turn++

            val currentDistance = p1.distanceOf(p2)

            if (currentDistance == 0) {
                return turn
            }

            if (speedingUp) {
                impossible = currentDistance >= distance
            } else {
                speedingUp = p1.speedingUp() && p2.speedingUp()
            }
            distance = currentDistance
        }

        return 0
    }

    val destroyTasks = hashMapOf<Int, MutableList<Pair<Day20Particle, Day20Particle>>>()

    for ((index, particle) in particles.withIndex()) {
        for (targetParticle in particles.drop(index + 1)) {
            val collideTurn = collide(particle.copy(), targetParticle.copy())
            if (collideTurn > 0) {

                destroyTasks.computeIfAbsent(collideTurn) { arrayListOf() } += particle to targetParticle
            }
        }
    }

    destroyTasks.entries.sortedBy { it.key }.forEach { (_, pairs) ->
        pairs.filter { !it.first.destroyed && !it.second.destroyed }.forEach {
            it.first.destroyed = true
            it.second.destroyed = true
        }
    }

    println(particles.count { !it.destroyed })
}

private data class Day20Particle(
    var index: Int,
    var px: Int,
    var py: Int,
    var pz: Int,
    var vx: Int,
    var vy: Int,
    var vz: Int,
    val ax: Int,
    val ay: Int,
    val az: Int
) {
    var destroyed: Boolean = false

    fun move(times: Int = 1) {
        repeat(times) {
            vx += ax
            px += vx

            vy += ay
            py += vy

            vz += az
            pz += vz
        }
    }

    fun distanceOf(target: Day20Particle): Int {
        return (px - target.px).absoluteValue + (py - target.py).absoluteValue + (pz - target.pz).absoluteValue
    }

    fun speedingUp(): Boolean {
        return speedingUp(px, ax) && speedingUp(py, ay) && speedingUp(pz, az)
    }

    private fun speedingUp(p: Int, a: Int): Boolean {
        return when {
            a > 0 -> p > 0
            a < 0 -> p < 0
            else -> true
        }
    }
}