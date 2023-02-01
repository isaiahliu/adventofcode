package y2021

import util.input

fun main() {
    fun IntRange.sub(target: IntRange): IntRange? {
        return (first.coerceAtLeast(target.first)..last.coerceAtMost(target.last)).takeIf { it.last - it.first >= 0 }
    }

    class Cube(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
        val size: Long = (xRange.last - xRange.first).takeIf { it >= 0 }?.let { xDiff ->
            (yRange.last - yRange.first).takeIf { it >= 0 }?.let { yDiff ->
                (zRange.last - zRange.first).takeIf { it >= 0 }?.let { zDiff ->
                    1L * (xDiff + 1) * (yDiff + 1) * (zDiff + 1)
                }
            }
        } ?: 0L

        fun intersect(target: Cube): Cube? {
            return xRange.sub(target.xRange)?.let { x ->
                yRange.sub(target.yRange)?.let { y ->
                    zRange.sub(target.zRange)?.let { z ->
                        Cube(x, y, z)
                    }
                }
            }
        }

        fun cover(target: Cube): Boolean {
            return intersect(target)?.size == target.size
        }

        fun breakCube(target: Cube): List<Cube> {
            return intersect(target)?.let {
                arrayOf(
                    Cube(target.xRange, target.yRange, target.zRange.first until it.zRange.first),
                    Cube(target.xRange, target.yRange, it.zRange.last + 1..target.zRange.last),
                    Cube(target.xRange.first until it.xRange.first, target.yRange, it.zRange),
                    Cube(it.xRange.last + 1..target.xRange.last, target.yRange, it.zRange),
                    Cube(it.xRange, target.yRange.first until it.yRange.first, it.zRange),
                    Cube(it.xRange, it.yRange.last + 1..target.yRange.last, it.zRange),
                ).filter { it.size > 0 }
            }.orEmpty()
        }
    }

    val cubes = arrayListOf<Cube>()

    val regex = "(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)".toRegex()
    input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1) }.forEach {
        val on = it[0] == "on"

        val cube = Cube(it[1].toInt()..it[2].toInt(), it[3].toInt()..it[4].toInt(), it[5].toInt()..it[6].toInt())

        if (on) {
            for (existingCube in cubes.toList()) {
                when {
                    existingCube.cover(cube) -> return@forEach
                    cube.intersect(existingCube) != null -> {
                        cubes -= existingCube
                        cubes += cube.breakCube(existingCube)
                    }
                }
            }

            cubes += cube
        } else {
            for (existingCube in cubes.toList()) {
                when {
                    cube.cover(existingCube) -> cubes -= existingCube
                    cube.intersect(existingCube) != null -> {
                        cubes -= existingCube
                        cubes += cube.breakCube(existingCube)
                    }
                }
            }
        }
    }

    val problem1Cube = Cube(-50..50, -50..50, -50..50)
    println(cubes.sumOf {
        it.intersect(problem1Cube)?.size ?: 0L
    })

    println(cubes.sumOf { it.size })
}