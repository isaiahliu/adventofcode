package y2018

import util.input

fun main() {
    val nums = input.map { it.toInt() }

    println(nums.sum())

    val frequency = hashSetOf<Int>()

    var sum = 0

    var index = 0
    while (true) {
        sum += nums[index]

        if (!frequency.add(sum)) {
            break
        }

        index = (index + 1) % nums.size
    }

    println(sum)
}