package y2021

import util.input

fun main() {
    val size = input.first().length

    val bitCounts = IntArray(size)

    input.forEach {
        it.forEachIndexed { index, c ->
            bitCounts[index] += if (c == '1') {
                1
            } else {
                -1
            }
        }
    }

    val result = bitCounts.reversed().foldIndexed(0) { index, acc, num ->
        if (num > 0) {
            acc + (1 shl index)
        } else {
            acc
        }
    }

    println(result * (result.inv().mod(1 shl size)))

    val oxygenNums = input.map { it.toCharArray() }.toMutableList()

    val co2Nums = input.map { it.toCharArray() }.toMutableList()

    fun findRating(nums: MutableList<CharArray>, most: Boolean): Int {
        val defaultChar = if (most) '1' else '0'

        var index = 0
        while (nums.size > 1) {
            var bitCount = 0

            nums.forEach {
                bitCount += if (it[index] == '1') {
                    1
                } else {
                    -1
                }
            }

            val keepChar = when {
                bitCount == 0 -> defaultChar
                most -> if (bitCount > 0) '1' else '0'
                else -> if (bitCount > 0) '0' else '1'
            }

            nums.removeAll {
                it[index] != keepChar
            }
            index++
        }

        return nums.first().reversed().foldIndexed(0) { index, acc, num ->
            acc + (1 shl index) * (num - '0')
        }
    }

    println(1L * findRating(oxygenNums, true) * findRating(co2Nums, false))
}

