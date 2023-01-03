package y2019

import util.input

fun main() {
    fun process(inputNum: Int): Int {
        var result = 0
        val nums = input.first().let {
            it.split(",").map { it.toInt() }.toIntArray()
        }

        var index = 0

        fun readParam(paramIndex: Int, positionMode: Boolean): Int {
            return if (positionMode) {
                nums[nums[index + paramIndex]]
            } else {
                nums[index + paramIndex]
            }
        }

        while (index < nums.size) {
            val t = nums[index]
            val instruction = t % 100
            val param1Mode = (t / 100) % 10 == 0
            val param2Mode = (t / 1000) % 10 == 0
            val param3Mode = (t / 10000) % 10 == 0

            when (instruction) {
                1 -> {
                    nums[nums[index + 3]] = readParam(1, param1Mode) + readParam(2, param2Mode)
                    index += 4
                }

                2 -> {
                    nums[nums[index + 3]] = readParam(1, param1Mode) * readParam(2, param2Mode)
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
                    if (readParam(1, param1Mode) != 0) {
                        index = readParam(2, param2Mode)
                    } else {
                        index += 3
                    }
                }

                6 -> {
                    if (readParam(1, param1Mode) == 0) {
                        index = readParam(2, param2Mode)
                    } else {
                        index += 3
                    }
                }

                7 -> {
                    nums[nums[index + 3]] = if (readParam(1, param1Mode) < readParam(2, param2Mode)) {
                        1
                    } else {
                        0
                    }
                    index += 4
                }

                8 -> {
                    nums[nums[index + 3]] = if (readParam(1, param1Mode) == readParam(2, param2Mode)) {
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