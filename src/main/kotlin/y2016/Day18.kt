package y2016

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        var dp = input.first().map { it == '^' }.toBooleanArray()

        repeat(400000) { index ->
            dp = BooleanArray(dp.size) {
                if (!dp[it]) {
                    if (index < 40) {
                        part1Result++
                    }
                    part2Result++
                }

                dp.getOrElse(it - 1) { false } xor dp.getOrElse(it + 1) { false }
            }
        }
    }
}