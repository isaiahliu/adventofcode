package y2017

import util.expect
import util.input
import kotlin.math.absoluteValue
import kotlin.math.sqrt

fun main() {
    expect(0, 1) {
        val target = input[0].toInt()

        val round = sqrt((target - 1).toDouble()).toInt().let { it - (it % 2 xor 1) }

        part1Result = ((target - round * round - 1) % (round + 1) - round / 2).absoluteValue + round / 2 + 1

        val nums = hashMapOf(0 to 0 to 1)
        var x = 0
        var y = 0

        while (part2Result <= target) {
            when {
                x > y.absoluteValue -> y++
                y >= x.absoluteValue && x + y > 0 -> x--
                -x >= y.absoluteValue && y - x > 0 -> y--
                else -> x++
            }

            part2Result = arrayOf(x - 1 to y - 1, x - 1 to y, x - 1 to y + 1, x to y - 1, x to y + 1, x + 1 to y - 1, x + 1 to y, x + 1 to y + 1).sumOf {
                nums[it] ?: 0
            }

            nums[x to y] = part2Result
        }
    }
}