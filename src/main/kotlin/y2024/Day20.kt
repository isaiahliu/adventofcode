package y2024

import util.expect
import util.input
import kotlin.math.absoluteValue

fun main() {
    expect(0, 0L) {
        val routes = arrayListOf<Pair<Int, Int>>()
        init@ for ((r, row) in input.withIndex()) {
            for ((c, ch) in row.withIndex()) {
                when (ch) {
                    'S' -> {
                        var curR = r
                        var curC = c

                        var direction = 0
                        val delta = arrayOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)
                        routes += r to c

                        while (true) {
                            val (dr, dc) = delta[direction]
                            curR += dr
                            curC += dc

                            when (input.getOrNull(curR)?.getOrNull(curC)) {
                                '#', null -> {
                                    curR -= dr
                                    curC -= dc
                                    direction = (direction + 1) % 4
                                }

                                'E' -> {
                                    routes += curR to curC
                                    break@init
                                }

                                else -> {
                                    routes += curR to curC

                                    direction = (direction + 3) % 4
                                }
                            }
                        }
                    }
                }
            }
        }

        routes.forEachIndexed { i, (sr, sc) ->
            for (j in i + 1 until routes.size) {
                val (er, ec) = routes[j]

                val originalDistance = j - i

                val distance = (sr - er).absoluteValue + (sc - ec).absoluteValue

                when {
                    originalDistance - distance < 100 -> {}
                    
                    distance <= 2 -> {
                        part1Result++
                    }

                    distance <= 20 -> {
                        part2Result++
                    }
                }
            }
        }

        part2Result += part1Result
    }
}