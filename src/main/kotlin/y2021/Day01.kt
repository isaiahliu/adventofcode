package y2021

import util.input

fun main() {
    val nums = input.map { it.toInt() }

    var t = nums.first()
    var result1 = 0
    nums.forEach {
        if (it > t) {
            result1++
        }
        t = it
    }

    println(result1)

    val t2 = nums.take(3).toIntArray()
    var index = 0
    var result2 = 0
    nums.drop(3).forEach {
        if (it > t2[index]) {
            result2++
        }

        t2[index] = it

        index++
        index %= 3
    }

    println(result2)
}