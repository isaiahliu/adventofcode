package y2018

import util.input

fun main() {
    val flatten = input.map {
        it.toCharArray().toList().groupingBy { it }.eachCount().values.filter { it in (2..3) }.distinct()
    }.flatten()
    val counts = flatten.groupingBy { it }.eachCount()

    println((counts[2] ?: 0) * (counts[3] ?: 0))

    val commons = hashSetOf<String>()
    for (leftIndex in input.indices) {
        val left = input[leftIndex]
        for (rightIndex in leftIndex + 1 until input.size) {
            val right = input[rightIndex]

            buildString {
                repeat(left.length.coerceAtMost(right.length)) {
                    if (left[it] == right[it]) {
                        append(left[it])
                    }
                }
            }.also {
                commons += it
            }
        }
    }

    println(commons.maxBy { it.length })
}