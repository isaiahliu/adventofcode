package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val dp = Array(input.size) { Array(input[it].length) { arrayListOf<Pair<Int, Int>>() } }

        input.indices.map { r -> input[r].indices.map { c -> r to c } }.flatten().groupBy { (r, c) -> input[r][c] - '0' }.entries.sortedByDescending { it.key }
            .forEach { (height, positions) ->
                when (height) {
                    9 -> {
                        positions.forEach { (r, c) ->
                            dp[r][c] += r to c
                        }
                    }

                    else -> {
                        positions.forEach { (r, c) ->
                            arrayOf(r - 1 to c, r + 1 to c, r to c - 1, r to c + 1).filter { (nr, nc) -> input.getOrNull(nr)?.getOrNull(nc)?.let { it - '0' } == height + 1 }
                                .map { (nr, nc) ->
                                    dp[r][c] += dp[nr][nc]
                                }

                            if (height == 0) {
                                part1Result += dp[r][c].distinct().size
                                part2Result += dp[r][c].size
                            }
                        }
                    }
                }
            }
    }
}