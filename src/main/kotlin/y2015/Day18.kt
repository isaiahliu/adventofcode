package y2015

import input

fun main() {
    val width = 100
    val lights1 = Array(width) { BooleanArray(width) }
    val lights2 = Array(width) { BooleanArray(width) }

    input.forEachIndexed { rowIndex, line ->
        val row1 = lights1[rowIndex]

        line.toCharArray().forEachIndexed { index, c ->
            row1[index] = c == '#'
        }

        val row2 = lights2[rowIndex]

        line.toCharArray().forEachIndexed { index, c ->
            row2[index] = c == '#'
        }
    }

    var previousPart1 = lights1
    var previousPart2 = lights2
    previousPart2[0][0] = true
    previousPart2[0][width - 1] = true
    previousPart2[width - 1][0] = true
    previousPart2[width - 1][width - 1] = true

    repeat(100) { step ->
        val currentPart1 = Array(width) { BooleanArray(width) }
        val currentPart2 = Array(width) { BooleanArray(width) }
        repeat(width) { row ->
            repeat(width) { column ->
                var temp1 = 0
                previousPart1.getOrNull(row - 1)?.getOrNull(column - 1)?.takeIf { it }?.also { temp1++ }
                previousPart1.getOrNull(row - 1)?.getOrNull(column)?.takeIf { it }?.also { temp1++ }
                previousPart1.getOrNull(row - 1)?.getOrNull(column + 1)?.takeIf { it }?.also { temp1++ }
                previousPart1.getOrNull(row)?.getOrNull(column - 1)?.takeIf { it }?.also { temp1++ }
                previousPart1.getOrNull(row)?.getOrNull(column + 1)?.takeIf { it }?.also { temp1++ }
                previousPart1.getOrNull(row + 1)?.getOrNull(column - 1)?.takeIf { it }?.also { temp1++ }
                previousPart1.getOrNull(row + 1)?.getOrNull(column)?.takeIf { it }?.also { temp1++ }
                previousPart1.getOrNull(row + 1)?.getOrNull(column + 1)?.takeIf { it }?.also { temp1++ }

                currentPart1[row][column] = if (previousPart1[row][column]) {
                    temp1 in (2..3)
                } else {
                    temp1 == 3
                }

                var temp2 = 0
                previousPart2.getOrNull(row - 1)?.getOrNull(column - 1)?.takeIf { it }?.also { temp2++ }
                previousPart2.getOrNull(row - 1)?.getOrNull(column)?.takeIf { it }?.also { temp2++ }
                previousPart2.getOrNull(row - 1)?.getOrNull(column + 1)?.takeIf { it }?.also { temp2++ }
                previousPart2.getOrNull(row)?.getOrNull(column - 1)?.takeIf { it }?.also { temp2++ }
                previousPart2.getOrNull(row)?.getOrNull(column + 1)?.takeIf { it }?.also { temp2++ }
                previousPart2.getOrNull(row + 1)?.getOrNull(column - 1)?.takeIf { it }?.also { temp2++ }
                previousPart2.getOrNull(row + 1)?.getOrNull(column)?.takeIf { it }?.also { temp2++ }
                previousPart2.getOrNull(row + 1)?.getOrNull(column + 1)?.takeIf { it }?.also { temp2++ }

                currentPart2[row][column] = if (previousPart2[row][column]) {
                    temp2 in (2..3)
                } else {
                    temp2 == 3
                }

                currentPart2[0][0] = true
                currentPart2[0][width - 1] = true
                currentPart2[width - 1][0] = true
                currentPart2[width - 1][width - 1] = true
            }
        }

        previousPart1 = currentPart1
        previousPart2 = currentPart2
    }

    println(previousPart1.sumOf { it.count { it } })
    println(previousPart2.sumOf { it.count { it } })
}