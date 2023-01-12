package y2020

import util.input

fun main() {
    val nums = input.map { it.toInt() }

    val num = nums.first { (2020 - it) in nums }

    println(num * (2020 - num))

    var result = 0
    loop@ for (num1 in nums) {
        for (num2 in nums) {
            if ((2020 - num1 - num2) in nums) {
                result = num1 * num2 * (2020 - num1 - num2)
                break@loop
            }
        }
    }

    println(result)
}