package y2020

import util.input

fun main() {
    val nums = input.map { it.toLong() }

    val preamble = Array(25) {
        nums[it]
    }

    var preambleOverrideIndex = 0

    var result1 = 0L
    for (num in nums.drop(preamble.size)) {
        if (preamble.indices.any { i1 -> preamble.indices.any { i2 -> i1 != i2 && preamble[i1] + preamble[i2] == num } }) {
            preamble[preambleOverrideIndex] = num
            preambleOverrideIndex += 1
            preambleOverrideIndex %= preamble.size
        } else {
            result1 = num
            break
        }
    }
    println(result1)

    var result2 = 0L
    loop@ for (firstIndex in 0 until nums.size - 1) {
        for (count in 2..nums.size - firstIndex) {
            val subNums = nums.drop(firstIndex).take(count)
            val sum = subNums.sum()

            when {
                sum > result1 -> {
                    break
                }

                sum == result1 -> {
                    result2 = subNums.min() + subNums.max()
                    break@loop
                }
            }
        }
    }

    println(result2)
}