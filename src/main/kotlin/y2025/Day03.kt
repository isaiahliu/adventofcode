package y2025

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        var t = 1L
        val times = LongArray(12) { t.also { t *= 10 } }
        input.forEach {
            val dp = arrayOfNulls<Long>(12)

            for (i in it.lastIndex downTo 0) {
                val digit = it[i] - '0'

                for (dpIndex in dp.lastIndex downTo 1) {
                    dp[dpIndex] = maxOf(dp[dpIndex] ?: 0L, (dp[dpIndex - 1] ?: continue) + digit * times[dpIndex])
                }
                dp[0] = maxOf(dp[0] ?: 0L, digit.toLong())
            }

            part1Result += dp[1] ?: 0
            part2Result += dp[11] ?: 0
        }
    }
}


