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
                when {
                    line[i] - line[i - 1] in 1..3 -> {
                        dp[i][0][0] = dp[i - 1][0][0]
                        dp[i][0][1] = dp[i - 1][0][1]
                    }

                    line[i] - line[i - 1] in -3..-1 -> {
                        dp[i][1][0] = dp[i - 1][1][0]
                        dp[i][1][1] = dp[i - 1][1][1]
                    }
                }

                when {
                    line[i] - line[i - 2] in 1..3 -> {
                        dp[i][0][1] = dp[i][0][1] || dp[i - 2][0][0]
                    }

                    line[i] - line[i - 2] in -3..-1 -> {
                        dp[i][1][1] = dp[i][1][1] || dp[i - 2][1][0]
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