package y2023

import util.input

fun main() {
    val times = input[0].split(" ").mapNotNull { it.toLongOrNull() }
    val distances = input[1].split(" ").mapNotNull { it.toLongOrNull() }

    fun calculate(time: Long, distance: Long): Long {
        val half = time / 2

        return if (half * (time - half) > distance) {
            var min = half
            var left = 1L
            var right = half

            while (left <= right) {
                val mid = (left + right) / 2

                if (mid * (time - mid) > distance) {
                    min = mid
                    right = mid - 1
                } else {
                    left = mid + 1
                }
            }

            (half - min + 1) * 2 - (1 - time % 2)
        } else {
            0
        }
    }

    val part1Result = times.indices.map {
        calculate(times[it], distances[it])
    }.fold(1L) { a, b ->
        a * b
    }

    val part2Result =
        calculate(input[0].replace("\\D".toRegex(), "").toLong(), input[1].replace("\\D".toRegex(), "").toLong())

    println(part1Result)
    println(part2Result)
}
