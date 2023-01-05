package y2019

import util.input
import java.util.*
import kotlin.math.pow

fun main() {
    val NORTH = 1L
    val SOUTH = 2L
    val WEST = 3L
    val EAST = 4L

    val WALL = 0L
    val MOVE = 1L
    val FOUND = 2L

    fun Long.opposite(): Long {
        return when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            WEST -> EAST
            EAST -> WEST
            else -> throw RuntimeException()
        }
    }

    fun process(): Pair<Int, Int> {
        var direction = NORTH

        var x = 0L
        var y = 0L

        var oxygenX = 0L
        var oxygenY = 0L

        val map = hashMapOf(x to y to MOVE)

        val route = LinkedList<Long>()
        val memory = input.first().let {
            it.split(",").map { it.toLong() }.toLongArray()
        }.mapIndexed { index, l ->
            index.toLong() to l
        }.toMap().toMutableMap()

        var index = 0L
        var relativeBase = 0L
        var done = false

        fun readParam(paramIndex: Long): Long {
            return when ((memory[index]!!.toInt() / 10 / (10.0.pow(paramIndex.toInt())).toInt()) % 10) {
                0 -> memory[memory[index + paramIndex] ?: 0L] ?: 0L
                1 -> memory[index + paramIndex] ?: 0L
                else -> memory[relativeBase + (memory[index + paramIndex] ?: 0L)] ?: 0L
            }
        }

        fun writeParam(paramIndex: Long, value: Long) {
            when ((memory[index]!!.toInt() / 10 / (10.0.pow(paramIndex.toInt())).toInt()) % 10) {
                0 -> memory[memory[index + paramIndex] ?: 0L] = value
                1 -> {
                    memory[index + paramIndex] = value
                }

                else -> {
                    memory[relativeBase + (memory[index + paramIndex] ?: 0L)] = value
                }
            }
        }

        while (!done) {
            when (memory[index]!!.toInt() % 100) {
                1 -> {
                    writeParam(3, readParam(1) + readParam(2))
                    index += 4
                }

                2 -> {
                    writeParam(3, readParam(1) * readParam(2))
                    index += 4
                }

                3 -> {
                    direction = if (x + 1 to y !in map) {
                        EAST
                    } else if (x - 1 to y !in map) {
                        WEST
                    } else if (x to y + 1 !in map) {
                        NORTH
                    } else if (x to y - 1 !in map) {
                        SOUTH
                    } else if (route.isEmpty()) {
                        done = true
                        NORTH
                    } else {
                        route.peek().opposite()
                    }

                    writeParam(1, direction)
                    index += 2
                }

                4 -> {
                    val ahead = readParam(1)

                    var targetX = x
                    var targetY = y

                    when (direction) {
                        NORTH -> {
                            targetY++
                        }

                        SOUTH -> {
                            targetY--
                        }

                        WEST -> {
                            targetX--
                        }

                        EAST -> {
                            targetX++
                        }
                    }

                    map[targetX to targetY] = ahead

//                    println("${targetX},${targetY}=${ahead}")

                    if (ahead == FOUND) {
                        oxygenX = targetX
                        oxygenY = targetY
                    }

                    if (ahead != WALL) {
                        if (route.isEmpty() || route.peek().opposite() != direction) {
                            route.push(direction)
                        } else {
                            route.pop()
                        }
                        x = targetX
                        y = targetY
                    }
                    index += 2
                }

                5 -> {
                    if (readParam(1) != 0L) {
                        index = readParam(2)
                    } else {
                        index += 3
                    }
                }

                6 -> {
                    if (readParam(1) == 0L) {
                        index = readParam(2)
                    } else {
                        index += 3
                    }
                }

                7 -> {
                    writeParam(
                        3, if (readParam(1) < readParam(2)) {
                            1
                        } else {
                            0
                        }
                    )
                    index += 4
                }

                8 -> {
                    writeParam(
                        3, if (readParam(1) == readParam(2)) {
                            1
                        } else {
                            0
                        }
                    )
                    index += 4
                }

                9 -> {
                    relativeBase += readParam(1)

                    index += 2
                }

                99 -> {
                    done = true
                }

                else -> {
                    println("Error")
                }
            }
        }

        var part1Result = 0

        val walked = hashSetOf(0L to 0L)
        val tasks = hashSetOf(0L to 0L)
        while (true) {
            if (oxygenX to oxygenY in tasks) {
                break
            }

            val current = tasks.toSet()
            tasks.clear()

            current.forEach { (x, y) ->
                arrayOf(x - 1 to y, x + 1 to y, x to y - 1, x to y + 1).forEach { (moveX, moveY) ->
                    if (map[moveX to moveY].takeIf { it == MOVE || it == FOUND } != null) {
                        if (walked.add(moveX to moveY)) {
                            tasks += moveX to moveY
                        }
                    }
                }
            }

            part1Result++
        }

        var part2Result = 0
        walked.clear()
        tasks.clear()
        walked += oxygenX to oxygenY
        tasks += oxygenX to oxygenY

        while (true) {
            val current = tasks.toSet()
            tasks.clear()

            current.forEach { (x, y) ->
                arrayOf(x - 1 to y, x + 1 to y, x to y - 1, x to y + 1).forEach { (moveX, moveY) ->
                    if (map[moveX to moveY].takeIf { it == MOVE } != null) {
                        if (walked.add(moveX to moveY)) {
                            tasks += moveX to moveY
                        }
                    }
                }
            }

            if (tasks.isEmpty()) {
                break
            }

            part2Result++
        }

        return part1Result to part2Result
    }

    val (part1Result, part2Result) = process()
    println(part1Result)
    println(part2Result)
}