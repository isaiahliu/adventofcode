package y2017

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        fun process(offset: Int.() -> Int): Int {
            val array = input.map { it.toInt() }.toIntArray()
            var index = 0
            var result = 0
            while (index in array.indices) {
                val jump = array[index]

                array[index] += jump.offset()

                index += jump
                result++
            }

            return result
        }

        part1Result = process { 1 }
        part2Result = process { if (this >= 3) -1 else 1 }
    }
}