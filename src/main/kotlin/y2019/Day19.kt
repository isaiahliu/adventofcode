package y2019

import util.input
import java.util.*
import kotlin.math.pow

fun main() {
    class IntCodeComputer(private val x: Int, private val y: Int, private val debug: Boolean = false) {
        fun process() {
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

        var result = 0L

        val queue = LinkedList<Long>().also {
            it += x.toLong()
            it += y.toLong()
        }

        fun inputReader(): Long {
            return queue.pop()
        }

        fun outputWriter(outputVal: Long) {
            if (debug) {
                println(outputVal)
            }
            result = outputVal
        }
    }

    var result1 = 0
    repeat(50) { y ->
        repeat(50) { x ->
            if (IntCodeComputer(x, y).also { it.process() }.result == 1L) {
                result1++
            }
        }
    }

    println(result1)

    val cache = hashMapOf<Pair<Int, Int>, Boolean>()

    fun read(x: Int, y: Int): Boolean {
        return cache.computeIfAbsent(x to y) {
            IntCodeComputer(x, y).also { it.process() }.result == 1L
        }
    }

    val SQUARE_SIZE = 100
    var y = 0
    var x = 0

    while (true) {
        while (!read(x, y + SQUARE_SIZE - 1)) {
            x++
        }

        if (read(x + SQUARE_SIZE - 1, y + SQUARE_SIZE - 1) && read(x, y) && read(x + SQUARE_SIZE - 1, y)) {
            break
        }

        y++
    }

    println(x * 10000 + y)
}