package y2022

import util.expectInt
import util.input
import java.util.*

fun main() {
    expectInt {

        val regex = "\\d+".toRegex()

        fun parse(line: String): List<Any> {
            val arrayStack = Stack<MutableList<Any>>()

            val root: MutableList<Any> = arrayListOf()
            var current = root

            var index = 0

            while (index < line.length) {
                when (line[index]) {
                    '[' -> {
                        arrayStack.push(current)

                        val temp = arrayListOf<Any>()

                        current.add(temp)
                        current = temp

                        index++
                    }

                    ']' -> {
                        current = arrayStack.pop()
                        index++
                    }

                    ',' -> {
                        index++
                    }

                    else -> {
                        regex.find(line, index)?.let { it.value }?.also {
                            current.add(it.toInt())

                            index += it.length
                        }
                    }
                }
            }

            return root
        }

        fun compare(array1: List<Any>, array2: List<Any>): Int {
            repeat(array1.size.coerceAtLeast(array2.size)) {
                val value1 = array1.getOrNull(it)
                val value2 = array2.getOrNull(it)

                if (value1 == null) {
                    return -1
                } else if (value2 == null) {
                    return 1
                } else if (value1 is Int && value2 is Int) {
                    if (value1 != value2) {
                        return if (value1 < value2) -1 else 1
                    }
                } else {
                    val subArray1 = value1.let { it as? List<Any> } ?: listOf(value1)
                    val subArray2 = value2.let { it as? List<Any> } ?: listOf(value2)

                    val comparison = compare(subArray1, subArray2)

                    if (comparison != 0) {
                        return comparison
                    }
                }
            }

            return 0
        }

        val arrays = arrayListOf<List<Any>>()

        for (index in input.indices step 3) {
            val line1 = input[index]
            val line2 = input[index + 1]

            val array1 = parse(line1)
            val array2 = parse(line2)

            val num = (index / 3) + 1
            arrays += array1
            arrays += array2

            val comparison = compare(array1, array2)

            if (comparison <= 0) {
                part1Result += num
            }
        }

        val divider1: List<Any> = listOf(listOf(listOf(2)))
        val divider2: List<Any> = listOf(listOf(listOf(6)))

        arrays += divider1
        arrays += divider2

        val sorted = arrays.sortedWith { o1, o2 ->
            compare(o1, o2)
        }

        part2Result = (sorted.indexOf(divider1) + 1) * (sorted.indexOf(divider2) + 1)
    }
}