package y2023

import util.expectLong
import util.input
import java.util.*

fun main() {
    expectLong {
        val start = input.indices.firstNotNullOf { r ->
            input[r].indices.firstOrNull { input[r][it] == 'S' }?.let { r to it }
        }

        val size = input.size

        val map = Array(size) { r ->
            BooleanArray(input[r].length) { c ->
                input[r][c] != '#'
            }
        }

        fun process(starts: Set<Pair<Int, Int>>, step: Int): Int {
            val visited = starts.toMutableSet()

            val current = LinkedList<Pair<Int, Int>>()
            current += starts

            repeat(step / 2) {
                repeat(current.size) {
                    val (r, c) = current.poll()

                    val nexts = hashSetOf<Pair<Int, Int>>()

                    if (map[(r - 1).mod(size)][c.mod(size)]) {
                        nexts += r - 2 to c
                        nexts += r - 1 to c - 1
                        nexts += r - 1 to c + 1
                    }

                    if (map[(r + 1).mod(size)][c.mod(size)]) {
                        nexts += r + 2 to c
                        nexts += r + 1 to c - 1
                        nexts += r + 1 to c + 1
                    }

                    if (map[r.mod(size)][(c - 1).mod(size)]) {
                        nexts += r to c - 2
                        nexts += r - 1 to c - 1
                        nexts += r + 1 to c - 1
                    }

                    if (map[r.mod(size)][(c + 1).mod(size)]) {
                        nexts += r to c + 2
                        nexts += r - 1 to c + 1
                        nexts += r + 1 to c + 1
                    }

                    nexts.filter {
                        map[it.first.mod(size)][it.second.mod(size)]
                    }.forEach {
                        if (visited.add(it)) {
                            current += it
                        }
                    }
                }
            }

            return visited.size
        }

        part1Result = process(setOf(start), 64).toLong()

        val oddStart = setOf(
            start.first - 1 to start.second,
            start.first + 1 to start.second,
            start.first to start.second - 1,
            start.first to start.second + 1,
        )
        val target = 26501365L
        val x = (target - 65) / 131
        val y0 = process(oddStart, 65)
        val y1 = process(setOf(start), 65 + 131)
        val y2 = process(oddStart, 65 + 131 * 2)

        val c = y0
        val a = (y2 - 2L * y1 + y0) / 2L
        val b = y1 - a - c

        part2Result = a * x * x + b * x + c
    }
}
