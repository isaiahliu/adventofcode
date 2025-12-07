package y2025

import util.expect
import util.input
import kotlin.math.sign

fun main() {
    expect(0, 0L) {
        val dp = LongArray(input[0].length)

        input.forEach {
            it.forEachIndexed { index, ch ->
                when (ch) {
                    'S' -> dp[index]++
                    '^' -> {
                        part1Result += dp[index].sign
                        dp[index - 1] += dp[index]
                        dp[index + 1] += dp[index]
                        dp[index] = 0
                    }
                }
            }
        }

        part2Result += dp.sum()
    }
}


