package y2024

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        val cache = hashMapOf<Long, Array<Long?>>()

        fun dfs(num: Long, depth: Int): Long {
            val numCache = cache.computeIfAbsent(num) { arrayOfNulls<Long>(76).also { it[0] = 1L } }

            numCache[depth]?.also { return it }

            return when {
                num == 0L -> {
                    dfs(1L, depth - 1)
                }

                num.toString().length % 2 == 0 -> {
                    val str = num.toString()

                    dfs(str.take(str.length / 2).toLong(), depth - 1) + dfs(str.drop(str.length / 2).toLong(), depth - 1)
                }

                else -> {
                    dfs(num * 2024, depth - 1)
                }
            }.also {
                numCache[depth] = it
            }
        }

        run {
            input[0].split(" ").forEach {
                it.toLongOrNull()?.also {
                    part1Result += dfs(it, 25)
                    part2Result += dfs(it, 75)
                }
            }
        }
    }
}