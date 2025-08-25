package y2015

import util.expect
import util.input

fun main() {
    expect(Long.MAX_VALUE, Long.MAX_VALUE) {
        val packages = input.map { it.toInt() }

        val totalSum = packages.sum()

        val packageSum1 = totalSum / 3
        val packageSum2 = totalSum / 4

        var dp = mutableMapOf<Int, Long>()

        packages.forEach { num ->
            val newDp = dp.toMutableMap()

            dp.forEach { (s, p) ->
                val sum = s + num
                val prod = p * num

                if (sum <= maxOf(packageSum1, packageSum2) && prod > 0) {
                    when (sum) {
                        packageSum1 -> {
                            part1Result = minOf(part1Result, prod)
                        }

                        packageSum2 -> {
                            part2Result = minOf(part2Result, prod)
                        }

                    }
                    newDp[sum] = minOf(newDp[sum] ?: Long.MAX_VALUE, prod)
                }
            }
            newDp[num] = minOf(newDp[num] ?: Long.MAX_VALUE, num.toLong())

            dp = newDp
        }
    }
}