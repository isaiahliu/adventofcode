package y2017

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        input.forEach {
            val nums = it.split("\\s+".toRegex()).map { it.toInt() }
            var min = Int.MAX_VALUE
            var max = Int.MIN_VALUE

            nums.forEach { num ->
                min = minOf(min, num)
                max = maxOf(max, num)

                nums.firstOrNull { it < num && num % it == 0 }?.also {
                    part2Result += num / it
                }
            }
            part1Result += max - min
        }
    }
}