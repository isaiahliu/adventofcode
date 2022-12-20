package y2017

import util.input

fun main() {
    val CLEAN = 0
    val WEAKENED = 1
    val INFECTED = 2
    val FLAGGED = 3

    fun process(burst: Int, step: Int): Int {
        val map = hashMapOf<Int, MutableMap<Int, Int>>()
        input.forEachIndexed { rowIndex, row ->
            row.toCharArray().map { if (it == '#') INFECTED else CLEAN }.forEachIndexed { columnIndex, node ->
                map.computeIfAbsent(rowIndex) { hashMapOf() }[columnIndex] = node
            }
        }

        var row = map.size / 2
        var column = (map[0]?.size ?: 0) / 2

        var direction = 0

        var result = 0
        repeat(burst) {
            val current = map[row]?.get(column) ?: 0

            direction = (direction + current + 3) % 4

            val newStatus = (current + step) % 4
            map.computeIfAbsent(row) { hashMapOf() }[column] = newStatus

            if (newStatus == INFECTED) {
                result++
            }

            when (direction) {
                0 -> {
                    row--
                }

                1 -> {
                    column++
                }

                2 -> {
                    row++
                }

                3 -> {
                    column--
                }
            }
        }

        return result
    }
    println(process(10000, 2))
    println(process(10000000, 1))
}