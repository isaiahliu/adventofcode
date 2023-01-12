package y2020

import util.input

fun main() {
    val nums = input.map { it.toInt() }

    val num = nums.first { (2020 - it) in nums }

    println(num * (2020 - num))

    for (num1 in nums) {
        val num2 = nums.firstOrNull { (2020 - num1 - it) in nums } ?: continue

        println(num1 * num2 * (2020 - num1 - num2))

        break
    }
}