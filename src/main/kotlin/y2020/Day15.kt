package y2020

import util.input

fun main() {
    val nums = input.first().split(",").map { it.toInt() }

    fun process(max: Int, debug: Boolean = false): Int {
        val map = nums.withIndex().associate {
            it.value to it.index
        }.toMutableMap()

        var num = nums.dropLast(1).lastIndexOf(nums.last()).takeIf { it != -1 }?.let { nums.size - it - 1 } ?: 0
        for (index in nums.size until max - 1) {
            val lastIndex = map[num]
            map[num] = index

            num = if (lastIndex == null) {
                0
            } else {
                index - lastIndex
            }
        }

        return num
    }

    println(process(2020))
    println(process(30000000))
}