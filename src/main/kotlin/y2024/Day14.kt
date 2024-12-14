package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        class Group {
            var innerParent: Group? = null
                private set

            var parent: Group
                set(value) {
                    innerParent = value
                }
                get() {
                    return innerParent?.parent?.also {
                        innerParent = it
                    } ?: this
                }

            fun join(target: Group) {
                val leftParent = parent
                val rightParent = target.parent

                if (leftParent != rightParent) {
                    leftParent.parent = rightParent
                }
            }
        }

        val regex = """^p=(\d+),(\d+) v=(-?\d+),(-?\d+)$""".toRegex()

        var q1 = 0
        var q2 = 0
        var q3 = 0
        var q4 = 0

        data class Robot(var x: Int, var y: Int, val vx: Int, val vy: Int) {
            fun move() {
                x = (x + vx).mod(101)
                y = (y + vy).mod(103)
            }

            fun quadrant(): Int? = when {
                x < 50 && y < 51 -> 1
                x > 50 && y < 51 -> 2
                x > 50 && y > 51 -> 3
                x < 50 && y > 51 -> 4
                else -> null
            }
        }

        val robots = input.mapNotNull {
            regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() }?.let { (px, py, vx, vy) ->
                Robot(px, py, vx, vy)
            }
        }

        var minSize = Int.MAX_VALUE
        repeat(10000) {
            val groups = Array(101) { arrayOfNulls<Group>(103) }
            robots.forEach {
                it.move()

                groups[it.x][it.y] = groups[it.x][it.y] ?: Group()
            }

            for (x in groups.indices) {
                for (y in groups[x].indices) {
                    groups[x][y]?.also {
                        groups.getOrNull(x - 1)?.getOrNull(y)?.join(it)
                        groups.getOrNull(x)?.getOrNull(y - 1)?.join(it)
                    }
                }
            }

            val size = groups.map { it.mapNotNull { it?.parent } }.flatten().distinct().size

            if (size < minSize) {
                minSize = size
                part2Result = it + 1
//                groups.forEach {
//                    println(it.joinToString("") {
//                        it?.let { "#" } ?: "."
//                    })
//                }
            }

            if (it == 99) {
                part1Result = robots.mapNotNull { it.quadrant() }.groupingBy { it }.eachCount().values.fold(1) { a, b -> a * b }
            }
        }
    }
}