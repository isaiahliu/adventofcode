package y2023

import util.expectInt
import util.input

fun main() {
    expectInt {
        input.forEach {
            val row = it.split(" ").map { it.toInt() }.toIntArray()

            var lastIndex = row.lastIndex

            var sign = 1
            var allZero = false
            while (!allZero) {
                allZero = true

                part1Result += row[lastIndex]
                part2Result += row[0] * sign
                sign *= -1

                (0 until lastIndex).forEach {
                    row[it] = row[it + 1] - row[it]
                    allZero = allZero && row[it] == 0
                }
                lastIndex--
            }
        }
    }
}