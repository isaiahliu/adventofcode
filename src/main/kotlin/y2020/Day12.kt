package y2020

import util.input
import kotlin.math.absoluteValue

fun main() {
    val NORTH = 0
    val EAST = 1
    val SOUTH = 2
    val WEST = 3

    fun process(v2: Boolean): Int {
        var direction = EAST

        val position = arrayOf(0, 0)
        val waypoint = arrayOf(10, 1)
        input.map { it[0] to it.drop(1).toInt() }.forEach { (method, count) ->
            var moveCount = count
            var moveDirection = direction

            when (method) {
                'N' -> {
                    moveDirection = NORTH
                }

                'S' -> {
                    moveDirection = SOUTH
                }

                'E' -> {
                    moveDirection = EAST
                }

                'W' -> {
                    moveDirection = WEST
                }

                'L' -> {
                    if (v2) {
                        repeat(count / 90) {
                            waypoint.reverse()
                            waypoint[0] = 0 - waypoint[0]
                        }
                    } else {
                        direction = (moveDirection - count / 90).mod(4)
                    }
                    moveCount = 0
                }

                'R' -> {
                    if (v2) {
                        repeat(count / 90) {
                            waypoint.reverse()
                            waypoint[1] = 0 - waypoint[1]
                        }
                    } else {
                        direction = (direction + count / 90).mod(4)
                    }
                    moveCount = 0
                }

                'F' -> {
                    if (v2) {
                        repeat(2) {
                            position[it] += waypoint[it] * moveCount
                        }

                        moveCount = 0
                    }
                }

                else -> throw RuntimeException(method.toString())
            }

            val t = if (v2) waypoint else position
            when (moveDirection) {
                NORTH -> {
                    t[1] += moveCount
                }

                SOUTH -> {
                    t[1] -= moveCount
                }

                EAST -> {
                    t[0] += moveCount
                }

                WEST -> {
                    t[0] -= moveCount
                }
            }
        }
        return position.sumOf { it.absoluteValue }
    }

    println(process(false))
    println(process(true))
}