package y2023

import util.input

fun main() {
    val UP = 0
    val RIGHT = 1
    val DOWN = 2
    val LEFT = 3

    var direction = RIGHT
    var row = 0
    var column = 0
    val chars = Array(input.size) { r ->
        CharArray(input[r].length) { c ->
            if (input[r][c] == 'S') {
                row = r
                column = c

                val joinLeft = input.getOrNull(r)?.get(c - 1)?.takeIf { it in setOf('-', 'L', 'F') } != null
                val joinRight = input.getOrNull(r)?.get(c + 1)?.takeIf { it in setOf('-', 'J', '7') } != null
                val joinUp = input.getOrNull(r - 1)?.get(c)?.takeIf { it in setOf('|', 'F', '7') } != null
                val joinDown = input.getOrNull(r + 1)?.get(c)?.takeIf { it in setOf('|', 'J', 'L') } != null

                when {
                    joinLeft && joinUp -> {
                        direction = UP
                        'J'
                    }

                    joinRight && joinDown -> {
                        direction = RIGHT
                        'F'
                    }

                    joinLeft && joinDown -> {
                        direction = DOWN
                        '7'
                    }

                    joinRight && joinUp -> {
                        direction = RIGHT
                        'L'
                    }

                    else -> {
                        '.'
                    }
                }

            } else {
                input[r][c]
            }
        }
    }

    val marks = Array(input.size) { BooleanArray(input[it].length) }

    var step = 0
    val lefts = hashSetOf<Pair<Int, Int>>()
    val rights = hashSetOf<Pair<Int, Int>>()

    while (!marks[row][column]) {
        step++
        marks[row][column] = true

        when (direction) {
            UP -> {
                lefts += row to column - 1
                rights += row to column + 1

                when (chars[row][column]) {
                    'F' -> {
                        lefts += row - 1 to column
                    }

                    '7' -> {
                        rights += row - 1 to column
                    }

                    'L' -> {
                        lefts += row + 1 to column
                    }

                    'J' -> {
                        rights += row + 1 to column
                    }
                }

                row--
                when (chars[row][column]) {
                    'F' -> {
                        direction = RIGHT
                    }

                    '7' -> {
                        direction = LEFT
                    }
                }
            }

            RIGHT -> {
                lefts += row - 1 to column
                rights += row + 1 to column

                when (chars[row][column]) {
                    'F' -> {
                        lefts += row to column - 1
                    }

                    'L' -> {
                        rights += row to column - 1
                    }

                    '7' -> {
                        lefts += row to column + 1
                    }

                    'J' -> {
                        rights += row to column + 1
                    }
                }

                column++
                when (chars[row][column]) {
                    'J' -> {
                        direction = UP
                    }

                    '7' -> {
                        direction = DOWN
                    }
                }
            }

            DOWN -> {
                rights += row to column - 1
                lefts += row to column + 1

                when (chars[row][column]) {
                    'F' -> {
                        rights += row - 1 to column
                    }

                    '7' -> {
                        lefts += row - 1 to column
                    }

                    'L' -> {
                        rights += row + 1 to column
                    }

                    'J' -> {
                        lefts += row + 1 to column
                    }
                }

                row++
                when (chars[row][column]) {
                    'J' -> {
                        direction = LEFT
                    }

                    'L' -> {
                        direction = RIGHT
                    }
                }
            }

            LEFT -> {
                lefts += row + 1 to column
                rights += row - 1 to column

                when (chars[row][column]) {
                    'F' -> {
                        rights += row to column - 1
                    }

                    'L' -> {
                        lefts += row to column - 1
                    }

                    '7' -> {
                        rights += row to column + 1
                    }

                    'J' -> {
                        lefts += row to column + 1
                    }
                }

                column--
                when (chars[row][column]) {
                    'L' -> {
                        direction = UP
                    }

                    'F' -> {
                        direction = DOWN
                    }
                }
            }
        }
    }

    val part1Result = step / 2

    fun Set<Pair<Int, Int>>.fill(): Int? {
        val remains = this.filter { (r, c) ->
            if (r !in input.indices || c !in input[0].indices) {
                return null
            }

            !marks[r][c]
        }.toMutableSet()

        val visited = remains.toMutableSet()

        while (remains.isNotEmpty()) {
            remains.toSet().also { remains.clear() }.forEach { (r, c) ->
                marks[r][c] = true

                arrayOf(r - 1 to c, r + 1 to c, r to c - 1, r to c + 1).forEach {
                    if (it.first !in input.indices || it.second !in input[0].indices) {
                        return null
                    }

                    if (!marks[it.first][it.second] && visited.add(it)) {
                        remains.add(it)
                    }
                }
            }
        }

        return visited.size
    }

    val part2Result = lefts.fill() ?: rights.fill()

    println(part1Result)
    println(part2Result)
}
