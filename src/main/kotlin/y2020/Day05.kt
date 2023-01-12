package y2020

import util.input

fun main() {
    fun IntRange.upperHalf(): IntRange {
        return first + (last - first) / 2 + 1..last
    }

    fun IntRange.lowerHalf(): IntRange {
        return first..first + (last - first) / 2
    }

    val seats = input.map {
        var row = 0..127
        var column = 0..7
        it.forEach {
            when (it) {
                'F' -> {
                    row = row.lowerHalf()
                }

                'B' -> {
                    row = row.upperHalf()
                }

                'L' -> {
                    column = column.lowerHalf()
                }

                'R' -> {
                    column = column.upperHalf()
                }
            }
        }
        row.first to column.first
    }

    println(seats.maxOf { (r, c) -> r * 8 + c })

    val front = seats.minOf { it.first }
    val back = seats.maxOf { it.first }
    val seatsIds = seats.filter { it.first != front && it.first != back }.map { (r, c) -> r * 8 + c }.sorted()

    var currentId = seatsIds.min() - 1

    for (seatsId in seatsIds) {
        if (seatsId - currentId == 2) {
            break
        }

        currentId = seatsId
    }

    println(currentId + 1)
}