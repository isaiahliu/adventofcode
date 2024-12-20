package y2024

import util.expect
import util.input
import java.util.*
import kotlin.math.absoluteValue

fun main() {
    expect(0, 0L) {
        data class State(val r: Int, val c: Int, val steps: Int) {
            val pos = r to c
        }

        fun calculateSteps(start: Pair<Int, Int>, end: Pair<Int, Int>): Array<IntArray> {
            val queue = PriorityQueue<State>(compareBy { it.steps })
            queue.add(State(start.first, start.second, 0))

            val visited = hashSetOf<Pair<Int, Int>>()
            val result = Array(input.size) {
                IntArray(input[0].length) {
                    99999999
                }
            }

            while (queue.isNotEmpty()) {
                val state = queue.poll()
                val ch = input.getOrNull(state.r)?.getOrNull(state.c)
                when {
                    ch == null -> {}
                    ch == '#' -> {}
                    visited.add(state.pos) -> {
                        result[state.r][state.c] = state.steps

                        queue.add(state.copy(r = state.r - 1, steps = state.steps + 1))
                        queue.add(state.copy(r = state.r + 1, steps = state.steps + 1))
                        queue.add(state.copy(c = state.c - 1, steps = state.steps + 1))
                        queue.add(state.copy(c = state.c + 1, steps = state.steps + 1))
                    }
                }
            }

            return result
        }

        lateinit var start: Pair<Int, Int>
        lateinit var end: Pair<Int, Int>

        input.forEachIndexed { r, row ->
            row.forEachIndexed { c, ch ->
                when (ch) {
                    'S' -> start = r to c
                    'E' -> end = r to c
                }
            }
        }

        val dis1 = calculateSteps(start, end)
        val dis2 = calculateSteps(end, start)

        val maxSteps = dis1[end.first][end.second]

        input.forEachIndexed { r, row ->
            row.forEachIndexed { c, _ ->
                for (dr in -20..20) {
                    val absR = dr.absoluteValue
                    val from = dis1.getOrNull(r)?.getOrNull(c)?.takeIf { it < maxSteps } ?: continue
                    for (dc in -20 + absR..20 - absR) {
                        val absC = dc.absoluteValue

                        val to = dis2.getOrNull(r + dr)?.getOrNull(c + dc)?.takeIf { it < maxSteps } ?: continue

                        val save = maxSteps - from - to - absR - absC

                        if (save >= 100) {
                            if (absR + absC <= 2) {
                                part1Result++
                                part2Result++
                            } else {
                                part2Result++
                            }
                        }
                    }
                }
            }
        }

    }
}