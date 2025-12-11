package y2025

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        val adjacent = hashMapOf<String, Set<String>>()
        input.forEach {
            it.split(" ").also {
                adjacent[it[0].dropLast(1)] = it.drop(0).toSet()
            }
        }

        val cache = hashMapOf<String, LongArray>()
        fun dfs(node: String): LongArray {
            return when (node) {
                "out" -> longArrayOf(1, 0, 0, 0)
                in cache -> cache[node] ?: LongArray(4)
                else -> {
                    val mark = when (node) {
                        "dac" -> 0b01
                        "fft" -> 0b10
                        else -> 0b00
                    }

                    val result = LongArray(4)

                    adjacent[node]?.forEach {
                        dfs(it).forEachIndexed { index, count ->
                            result[index or mark] += count
                        }
                    }

                    cache[node] = result
                    result
                }
            }
        }

        part1Result = dfs("you").sum()
        part2Result = dfs("svr")[0b11]
    }
}
