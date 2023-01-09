package y2019

import util.input
import kotlin.math.pow

fun main() {
    fun process(inputReader: () -> Long, outputWriter: (Long) -> Unit) {
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
                    writeParam(1, inputReader())
                    index += 2
                }

                4 -> {
                    outputWriter(readParam(1))
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
    }

    var result1 = 0L
    val inputReader1 = { 1L }
    val outputWriter1: (Long) -> Unit = {
        result1 = it
    }
    process(inputReader1, outputWriter1)
    println(result1)

    var result2 = 0L
    val inputReader2 = { 2L }
    val outputWriter2: (Long) -> Unit = {
        result2 = it
    }
    process(inputReader2, outputWriter2)
    println(result2)
}