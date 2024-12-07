package y2024

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        input.map { it.split(":") }.map { (left, right) ->
            left.toLong() to right.split(" ").mapNotNull { it.toLongOrNull() }
        }.forEach { (sum, nums) ->
            fun dfs(s: Long, index: Int, useCombine: Boolean): Boolean {
                val num = nums[index]
                return when {
                    index == 0 -> s == num
                    s >= num && dfs(s - num, index - 1, useCombine) -> true
                    s % num == 0L && dfs(s / num, index - 1, useCombine) -> true
                    else -> useCombine
                            && (s - num).toString().let { it.length - maxOf(it.trimEnd('0').length, 1) } >= num.toString().length
                            && dfs(s.toString().dropLast(num.toString().length).toLong(), index - 1, true)
                }
            }

            when {
                dfs(sum, nums.lastIndex, false) -> {
                    part1Result += sum
                    part2Result += sum
                }

                dfs(sum, nums.lastIndex, true) -> {
                    part2Result += sum
                }
            }
        }
    }
}
