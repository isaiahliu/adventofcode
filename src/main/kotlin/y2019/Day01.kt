package y2019

import util.input

fun main() {
    val nums = input.map { it.toInt() }

    println(nums.sumOf { it / 3 - 2 })

    var result = 0
    var t = nums
    while (t.isNotEmpty()) {
        t = t.map { it / 3 - 2 }.filter { it > 0 }

        result += t.sum()
    }

    println(result)
}