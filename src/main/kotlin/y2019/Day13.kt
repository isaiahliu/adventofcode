package y2019

import util.input
import kotlin.math.pow

fun main() {
    fun Map<Pair<Int, Int>, Int>.printMap() {
        val map = this
        for (y in map.minOf { it.key.second }..map.maxOf { it.key.second }) {
            buildString {
                for (x in map.minOf { it.key.first }..map.maxOf { it.key.first }) {
                    append(
                        when (map[x to y]) {
                            1 -> "#"
                            2 -> "."
                            3 -> "-"
                            4 -> "o"
                            else -> " "
                        }
                    )
                }
            }.also {
                println(it)
            }
        }
        println()
    }

    fun process(initialValue: Long? = null): Pair<Int, HashMap<Pair<Int, Int>, Int>> {
        val map = hashMapOf<Pair<Int, Int>, Int>()
        var score = 0
        val positions = arrayListOf<Int>()

        val memory = input.first().let {
            it.split(",").map { it.toLong() }.toLongArray()
        }.mapIndexed { index, l ->
            index.toLong() to l
        }.toMap().toMutableMap()

        initialValue?.also {
            memory[0L] = it
        }

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
                    val ballX = map.entries.first { it.value == 4 }.key.first
                    val padX = map.entries.first { it.value == 3 }.key.first

                    val inputNum = when {
                        ballX - padX > 0 -> 1L
                        ballX - padX < 0 -> -1L
                        else -> 0L
                    }

                    writeParam(1, inputNum)
                    index += 2
                }

                4 -> {
                    val output = readParam(1)

                    when (positions.size) {
                        2 -> {
                            if (positions[0] == -1 && positions[1] == 0 && output !in (0..4)) {
                                score = output.toInt()
                            } else {
                                map[positions[0] to positions[1]] = output.toInt()
                            }

                            positions.clear()
                        }

                        else -> {
                            positions += output.toInt()
                        }
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
        return score to map
    }

    val map = process().second
    println(map.count { it.value == 2 })

    println(process(2).first)
}