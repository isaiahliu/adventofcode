package y2019

import util.input
import kotlin.math.pow

fun main() {
    fun process(inputNum: Long): Long {
        var result = 0L
        val nums = input.first().let {
            it.split(",").map { it.toLong() }.toLongArray()
        }.mapIndexed { index, l ->
            index.toLong() to l
        }.toMap().toMutableMap()

        var index = 0L
        var relativeBase = 0L
        var done = false

        fun readParam(paramIndex: Long): Long {
            return when ((nums[index]!!.toInt() / 10 / (10.0.pow(paramIndex.toInt())).toInt()) % 10) {
                0 -> nums[nums[index + paramIndex] ?: 0L] ?: 0L
                1 -> nums[index + paramIndex] ?: 0L
                else -> nums[relativeBase + (nums[index + paramIndex] ?: 0L)] ?: 0L
            }
        }

        fun writeParam(paramIndex: Long, value: Long) {
            when ((nums[index]!!.toInt() / 10 / (10.0.pow(paramIndex.toInt())).toInt()) % 10) {
                0 -> nums[nums[index + paramIndex] ?: 0L] = value
                1 -> {
                    nums[index + paramIndex] = value
                }

                else -> {
                    nums[relativeBase + (nums[index + paramIndex] ?: 0L)] = value
                }
            }
        }

        while (!done) {
            when (nums[index]!!.toInt() % 100) {
                1 -> {
                    writeParam(3, readParam(1) + readParam(2))
                    index += 4
                }

                2 -> {
                    writeParam(3, readParam(1) * readParam(2))
                    index += 4
                }

                3 -> {
                    writeParam(1, inputNum)
                    index += 2
                }

                4 -> {
                    result = readParam(1)
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
        return result
    }

    println(process(1L))
    println(process(2L))
}