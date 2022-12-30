package y2019

import util.input

fun main() {
    val (min, max) = input.first().let { it.split("-") }.map { it.toInt() }

    fun process(v2: Boolean): Int {
        var result = 0
        for (num in min..max) {
            val digits = num.toString().padStart(6, '0').toCharArray().map { it - '0' }

            val match = if (v2) {
                var digit = digits[0]
                var count = 1
                val counts = hashSetOf<Int>()
                repeat(5) {
                    if (digits[it + 1] == digit) {
                        count++
                    } else {
                        counts += count
                        digit = digits[it + 1]
                        count = 1
                    }
                }

                counts += count

                2 in counts
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