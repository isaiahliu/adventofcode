package y2019

import util.input

fun main() {
    val (min, max) = input.first().split("-").map { it.toInt() }

    fun process(v2: Boolean): Int {
        var result = 0
        for (num in min..max) {
            val digits = num.toString().padStart(6, '0').toCharArray().map { it - '0' }

            val match = if (v2) {
                (0 until 5).any {
                    digits[it] == digits[it + 1] && digits[it] != digits.getOrNull(it + 2) && digits[it] != digits.getOrNull(it - 1)
                }
            } else {
                digits.distinct().size < 6
            }
            if ((0 until 5).all { digits[it] <= digits[it + 1] } && match) {
                result++
            }
        }

        return result
    }
    println(process(false))
    println(process(true))
}