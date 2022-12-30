package y2019

import util.input

fun main() {
    fun process(noun: Int, verb: Int): Int {
        val nums = input.first().let {
            it.split(",").map { it.toInt() }.toIntArray()
        }
        nums[1] = noun
        nums[2] = verb

        var index = 0
        while (true) {
            when (nums[index]) {
                1 -> {
                    nums[nums[index + 3]] = nums[nums[index + 1]] + nums[nums[index + 2]]
                    index += 4
                }

                2 -> {
                    nums[nums[index + 3]] = nums[nums[index + 1]] * nums[nums[index + 2]]
                    index += 4
                }

                99 -> {
                    break
                }
            }
        }
        return nums[0]
    }

    println(process(12, 2))

    repeat(100) { noun ->
        repeat(100) { verb ->
            if (process(noun, verb) == 19690720) {
                println(100 * noun + verb)

                return
            }
        }
    }
}