package y2024

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        input.map { it.split(":") }.map { (left, right) ->
            left.toLong() to right.split(" ").mapNotNull { it.toLongOrNull() }
        }.forEach { (sum, nums) ->
            fun dfs(s: Long, index: Int, useCombine: Boolean): Boolean {
                return when {
                    index == nums.size -> s == sum
                    s > sum -> false
                    dfs(s + nums[index], index + 1, useCombine) -> true
                    dfs(s * nums[index], index + 1, useCombine) -> true
                    useCombine -> dfs("${s}${nums[index]}".toLong(), index + 1, useCombine)
                    else -> false
                }
            }

            if (dfs(nums[0], 1, false)) {
                part1Result += sum
                part2Result += sum
            } else if (dfs(nums[0], 1, true)) {
                part2Result += sum
            }
        }
    }
}
