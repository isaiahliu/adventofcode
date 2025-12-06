package y2025

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        val numbers1 = arrayListOf<MutableList<Int>>()
        input.forEach {
            it.split(" ").filter { it.isNotBlank() }.also {
                it.forEachIndexed { index, node ->
                    when (node) {
                        "+" -> part1Result += numbers1[index].sum()
                        "*" -> part1Result += numbers1[index].fold(1L) { a, b ->
                            a * b
                        }

                        else -> {
                            if (numbers1.size <= index) {
                                numbers1.add(arrayListOf())
                            }

                            numbers1[index] += node.toInt()
                        }
                    }
                }
            }
        }

        var num = 0
        val nums = arrayListOf<Int>()
        for (c in input.maxOf { it.lastIndex } downTo 0) {
            for (r in input.indices) {
                when (val ch = input[r].getOrNull(c)) {
                    ' ', null -> Unit

                    '+' -> {
                        part2Result += nums.sum() + num
                        nums.clear()
                        num = 0
                    }

                    '*' -> {
                        part2Result += nums.fold(num.toLong()) { a, b -> a * b }
                        nums.clear()
                        num = 0
                    }

                    else -> {
                        num *= 10
                        num += ch - '0'
                    }
                }
            }

            if (num > 0) {
                nums.add(num)
                num = 0
            }
        }
    }
}


