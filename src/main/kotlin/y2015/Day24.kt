package y2015

import util.expect
import util.input

fun main() {
    expect(Long.MAX_VALUE, Long.MAX_VALUE) {
        val packages = input.map { it.toInt() }

        val packageSum1 = packages.sum() / 3
        val packageSum2 = packages.sum() / 4

        var dp = mutableMapOf<Int, Long>()

        packages.forEach { p ->
            val newDp = dp.toMutableMap()

            dp.forEach { (sum, prod) ->
                val newSum = sum + p
                val newProd = prod * p

                if (newSum <= maxOf(packageSum1, packageSum2) && newProd > 0) {
                    when (newSum) {
                        packageSum1 -> {
                            part1Result = minOf(part1Result, newProd)
                        }

                        packageSum2 -> {
                            part2Result = minOf(part2Result, newProd)
                        }

                    }
                    newDp[newSum] = minOf(newDp[newSum] ?: Long.MAX_VALUE, newProd)
                }
            }
            newDp[p] = minOf(newDp[p] ?: Long.MAX_VALUE, p.toLong())

            dp = newDp
        }
    }
}