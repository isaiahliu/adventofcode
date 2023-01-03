package y2019

import util.input
import kotlin.math.pow

fun main() {
    fun process(inputNum: Int): Int {
        var result = 0
        val nums = input.first().let {
            it.split(",").map { it.toInt() }.toIntArray()
        }

        var index = 0

        fun readParam(paramIndex: Int): Int {
            val paramMode = (nums[index] / 10 / (10.0.pow(paramIndex)).toInt()) % 10 == 0

            return if (paramMode) {
                nums[nums[index + paramIndex]]
            } else {
                nums[index + paramIndex]
            }
        }

        while (index < nums.size) {
            when (nums[index] % 100) {
                1 -> {
                    nums[nums[index + 3]] = readParam(1) + readParam(2)
                    index += 4
                }

                2 -> {
                    nums[nums[index + 3]] = readParam(1) * readParam(2)
                    index += 4
                }

                3 -> {
                    nums[nums[index + 1]] = inputNum
                    index += 2
                }

                4 -> {
                    result = nums[nums[index + 1]]
                    index += 2
                }

                5 -> {
                    if (readParam(1) != 0) {
                        index = readParam(2)
                    } else {
                        index += 3
                    }
                }

                6 -> {
                    if (readParam(1) == 0) {
                        index = readParam(2)
                    } else {
                        index += 3
                    }
                }

                7 -> {
                    nums[nums[index + 3]] = if (readParam(1) < readParam(2)) {
                        1
                    } else {
                        0
                    }
                    index += 4
                }

                8 -> {
                    nums[nums[index + 3]] = if (readParam(1) == readParam(2)) {
                        1
                    } else {
                        0
                    }
                    index += 4
                }

                99 -> {
                    break
                }

                else -> {
                    println("Error")
                }
            }
        }
        return result
    }

    println(process(1))
    println(process(5))
}