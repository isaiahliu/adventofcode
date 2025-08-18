package y2015

import util.expect
import util.forEachBit
import util.input

fun main() {
    expect(0, Int.MAX_VALUE) {
        val bottles = input.map { it.toInt() }.toIntArray()

        repeat(1 shl bottles.size) {
            var sum = 0
            var count = 0

            it.forEachBit {
                sum += bottles[it]
                count++
            }

            if (sum == 150) {
                part1Result++
                part2Result = minOf(part2Result, count)
            }
        }
    }
}

