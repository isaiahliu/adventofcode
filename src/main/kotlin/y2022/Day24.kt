package y2022

import util.input

fun main() {
    val UP = '^'
    val DOWN = 'v'
    val LEFT = '<'
    val RIGHT = '>'

    val map = input.drop(1).dropLast(1).map {
        it.drop(1).dropLast(1)
    }.toTypedArray()

    val width = map[0].length
    val height = map.size

    fun hasBlizzard(rowIndex: Int, columnIndex: Int, turn: Int): Boolean {
        return when {
            rowIndex !in 0 until height || columnIndex !in 0 until width -> {
                true
            }

            map[(rowIndex + turn).mod(height)][columnIndex] == UP -> {
                true
            }

            map[(rowIndex - turn).mod(height)][columnIndex] == DOWN -> {
                true
            }

            map[rowIndex][(columnIndex + turn).mod(width)] == LEFT -> {
                true
            }

            map[rowIndex][(columnIndex - turn).mod(width)] == RIGHT -> {
                true
            }

            else -> false
        }
    }


    fun process(startTurn: Int, start: Pair<Int, Int>, goal: Pair<Int, Int>): Int {
        val tasks = hashSetOf(start)
        var turn = startTurn

        while (true) {
            turn++

            if (tasks.any { (r, c) -> r == goal.first && c == goal.second }) {
                break
            }

            val current = tasks.toSet()
            tasks.clear()

            current.forEach { (rowIndex, columnIndex) ->
                val rightSafe = !hasBlizzard(rowIndex, columnIndex + 1, turn)
                val downSafe = !hasBlizzard(rowIndex + 1, columnIndex, turn)
                val staySafe = !hasBlizzard(rowIndex, columnIndex, turn)
                val leftSafe = !hasBlizzard(rowIndex, columnIndex - 1, turn)
                val upSafe = !hasBlizzard(rowIndex - 1, columnIndex, turn)

                if (rightSafe) {
                    tasks += rowIndex to columnIndex + 1
                }

                if (downSafe) {
                    tasks += rowIndex + 1 to columnIndex
                }

                if (staySafe) {
                    tasks += rowIndex to columnIndex
                }

                if (upSafe) {
                    tasks += rowIndex - 1 to columnIndex
                }

                if (leftSafe) {
                    tasks += rowIndex to columnIndex - 1
                }
            }

            if (tasks.isEmpty()) {
                tasks += start
            }
        }

        return turn
    }

    val first = process(0, -1 to 0, height - 1 to width - 1)
    println(first)

    val second = process(first, height - 1 to width, 0 to 0)
    val third = process(second, -1 to 0, height - 1 to width - 1)
    println(third)
}