package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        class Group {
            var size = 1
            var innerParent: Group? = null
                private set

            var parent: Group
                set(value) {
                    innerParent = value
                    value.size += size
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

        val WIDTH = 101
        val HEIGHT = 103
        val regex = """^p=(\d+),(\d+) v=(-?\d+),(-?\d+)$""".toRegex()

        class Robot(str: String) {
            var x: Int
            var y: Int
            val vx: Int
            val vy: Int

            init {
                regex.matchEntire(str)?.groupValues?.drop(1)?.map { it.toInt() }.orEmpty().also {
                    x = it.getOrElse(0) { 0 }
                    y = it.getOrElse(1) { 0 }
                    vx = it.getOrElse(2) { 0 }
                    vy = it.getOrElse(3) { 0 }
                }
            }

            fun move() {
                x = (x + vx).mod(WIDTH)
                y = (y + vy).mod(HEIGHT)
            }

            fun quadrant(second: Int): Int? = when {
                (x + vx * second).mod(WIDTH) < WIDTH / 2 && (y + vy * second).mod(HEIGHT) < HEIGHT / 2 -> 1
                (x + vx * second).mod(WIDTH) > WIDTH / 2 && (y + vy * second).mod(HEIGHT) < HEIGHT / 2 -> 2
                (x + vx * second).mod(WIDTH) > WIDTH / 2 && (y + vy * second).mod(HEIGHT) > HEIGHT / 2 -> 3
                (x + vx * second).mod(WIDTH) < WIDTH / 2 && (y + vy * second).mod(HEIGHT) > HEIGHT / 2 -> 4
                else -> null
            }
        }

        val robots = input.map {
            Robot(it)
        }

        part1Result = robots.mapNotNull { it.quadrant(100) }.groupingBy { it }.eachCount().values.fold(1) { a, b -> a * b }

        var second = 0

        while (true) {
            second++
            val groups = hashMapOf<Pair<Int, Int>, Group>()

            var maxSize = 0
            robots.forEach {
                it.move()

                groups.computeIfAbsent(it.x to it.y) { (x, y) ->
                    Group().also { group ->
                        groups[x - 1 to y]?.join(group)
                        groups[x + 1 to y]?.join(group)
                        groups[x to y - 1]?.join(group)
                        groups[x to y + 1]?.join(group)

                        maxSize = maxOf(maxSize, group.parent.size)
                    }
                }
            }

            if (maxSize > 100) {
                part2Result = second
                doAfter {
                    buildString {
                        repeat(HEIGHT) { y ->
                            repeat(WIDTH) { x ->
                                append(groups[x to y]?.let { "." } ?: " ")
                            }
                            appendLine()
                        }
                    }.also { println(it) }
                }
                break
            }
        }
    }
}