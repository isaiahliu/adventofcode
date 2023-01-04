package y2019

import util.input
import kotlin.math.pow

fun main() {
    fun process(points: MutableMap<Pair<Int, Int>, Long>): MutableMap<Pair<Int, Int>, Long> {
        var x = 0
        var y = 0
        //UP
        var direction = 0

        var readingDirection = false

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
                    writeParam(1, points[x to y] ?: 0L)
                    index += 2
                }

                4 -> {
                    val output = readParam(1)

                    if (readingDirection) {
                        direction = when (output) {
                            0L -> (direction - 1).mod(4)
                            else -> (direction + 1).mod(4)
                        }

                        when (direction) {
                            0 -> y--
                            1 -> x++
                            2 -> y++
                            3 -> x--
                        }
                    } else {
                        points[x to y] = output
                    }

                    readingDirection = !readingDirection

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
        return points
    }

    println(process(hashMapOf()).size)

    val points = process(hashMapOf(0 to 0 to 1))
    for (y in points.keys.minOf { it.second }..points.keys.maxOf { it.second }) {
        buildString {
            for (x in points.keys.minOf { it.first }..points.keys.maxOf { it.first }) {
                append(if (points[x to y] == 1L) "#" else " ")
            }
        }.also {
            println(it)
        }
    }
}