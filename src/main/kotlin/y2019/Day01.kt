package y2019

import util.input

fun main() {
    val nums = input.map { it.toInt() }

    println(nums.sumOf { it / 3 - 2 })

    println(nums.sumOf {
        var sum = 0
        var t = it
        while (true) {
            t = t / 3 - 2

            if (t > 0) {
                sum += t
            } else {
                break
            }

        }
        sum
    })
}