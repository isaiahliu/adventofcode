package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        input.map { it.split(" ").mapNotNull { it.toIntOrNull() } }.forEach { line ->
            val dp = Array(line.size) {//nums
                Array(2) {//inc,dec
                    BooleanArray(2)//tolerance 0,1
                }
            }

            dp[0][0][0] = true
            dp[0][1][0] = true

            when (line[1] - line[0]) {
                in 1..3 -> {
                    dp[1][0][0] = true
                    dp[1][1][1] = true
                }

                in -3..-1 -> {
                    dp[1][1][0] = true
                    dp[1][0][1] = true
                }

                else -> {
                    dp[1][0][1] = true
                    dp[1][1][1] = true
                }
            }

            for (i in 2 until line.size) {
                val cur = line[i]
                val curDp = dp[i]
                val last = line[i - 1]
                val lastDp = dp[i - 1]

                when {
                    cur - last in 1..3 -> {
                        curDp[0][0] = lastDp[0][0]
                        curDp[0][1] = lastDp[0][1]
                    }

                    cur - last in -3..-1 -> {
                        curDp[1][0] = lastDp[1][0]
                        curDp[1][1] = lastDp[1][1]
                    }
                }

                when {
                    cur - line[i - 2] in 1..3 -> {
                        curDp[0][1] = curDp[0][1] || dp[i - 2][0][0]
                    }

                    cur - line[i - 2] in -3..-1 -> {
                        curDp[1][1] = curDp[1][1] || dp[i - 2][1][0]
                    }
                }
            }

            when {
                dp.last().any { it[0] } -> {
                    part1Result++
                    part2Result++
                }

                dp.last().any { it[1] } || dp[dp.lastIndex - 1].any { it[0] } -> {
                    part2Result++
                }
            }
        }
    }
}